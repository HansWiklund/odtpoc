package se.inera.odp.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.WebUtils;

import se.inera.odp.core.exception.*;
import se.inera.odp.core.utils.ResponseLoggerMapper;

@RestControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler {

	Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	
	@Autowired
	ResponseLoggerMapper responseMapper;
    
    @ExceptionHandler(value = { ODPAuthorizationException.class })
    protected ResponseEntity<Object> handleODPAuthorizationException(RuntimeException ex, WebRequest request) {
        Map<String, Object> bodyOfResponse = responseMapper.errorMessage(HttpStatus.FORBIDDEN, ex.getMessage(), responseMapper.getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { ODPException.class })
    protected ResponseEntity<Object> handleODPException(ODPException ex, WebRequest request) {
    	Map<String, Object>  bodyOfResponse = responseMapper.errorMessage(ex.getStatus(), ex.getMessage(), responseMapper.getUri(request), ex.getErrCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatus(), request);
    }
    
    @ExceptionHandler(value = { HttpStatusCodeException.class, HttpServerErrorException.class, HttpClientErrorException.class})
    protected ResponseEntity<Object> handleRestClientException(HttpStatusCodeException ex, WebRequest request) {
    	
    	if(ex.getStatusCode().equals(HttpStatus.FORBIDDEN) || ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
    		return handleODPAuthorizationException(ex, request);
    	else {
	        Map<String, Object> bodyOfResponse = responseMapper.errorMessage(ex.getStatusCode(), "CKAN response error", responseMapper.getUri(request));
	        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatusCode(), request);
    	}
    }

    @ExceptionHandler(value = { RestClientException.class})
    protected ResponseEntity<Object> handleRestClientException(RestClientException ex, WebRequest request) {
    	Map<String, Object>  bodyOfResponse = responseMapper.errorMessage(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error", responseMapper.getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
 
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException ex) {
        Map<String, Object> bodyOfResponse = responseMapper.errorMessage(HttpStatus.NOT_FOUND, "Requested resource was not found", request.getRequestURI());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        Map<String, Object> bodyOfResponse = responseMapper.errorMessage(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error", request.getDescription(false));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    
	private ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<>(body, headers, status);
	}
	
	private ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status) {

		return new ResponseEntity<>(body, headers, status);
	}
	
}
