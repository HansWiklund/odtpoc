package se.inera.odp.core.controller;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.WebUtils;

import se.inera.odp.core.exception.*;

@RestControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler {

	Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    
    @ExceptionHandler(value = { ODPAuthorizationException.class })
    protected ResponseEntity<Object> handleODPAuthorizationException(RuntimeException ex, WebRequest request) {
        Map<String, Object> bodyOfResponse = message(HttpStatus.FORBIDDEN, ex.getMessage(), getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { ODPException.class })
    protected ResponseEntity<Object> handleODPException(ODPException ex, WebRequest request) {
    	Map<String, Object>  bodyOfResponse = message(ex.getStatus(), ex.getMessage(), getUri(request), ex.getErrCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatus(), request);
    }
    
    @ExceptionHandler(value = { HttpStatusCodeException.class, HttpServerErrorException.class, HttpClientErrorException.class})
    protected ResponseEntity<Object> handleRestClientException(HttpStatusCodeException ex, WebRequest request) {
    	
    	if(ex.getStatusCode().equals(HttpStatus.FORBIDDEN) || ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
    		return handleODPAuthorizationException(ex, request);
    	else {
	        Map<String, Object> bodyOfResponse = message(ex.getStatusCode(), "CKAN response error", getUri(request));
	        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatusCode(), request);
    	}
    }

    @ExceptionHandler(value = { RestClientException.class})
    protected ResponseEntity<Object> handleRestClientException(RestClientException ex, WebRequest request) {
    	Map<String, Object>  bodyOfResponse = message(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error", getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
 
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException ex) {
        Map<String, Object> bodyOfResponse = message(HttpStatus.NOT_FOUND, "Requested resource was not found", request.getRequestURI());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        Map<String, Object> bodyOfResponse = message(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error", request.getDescription(false));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private Map<String, Object> message(HttpStatus status, String msg, String url ) {
    	return message(status, msg, url, null);
    }
    
    private Map<String, Object> message(HttpStatus status, String msg, String url, String errCode ) {
    	
    	LocalDateTime localDate = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", localDate.toString());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        if(errCode != null)
        	response.put("error_code", errCode);
        response.put("message", msg);
        response.put("path", url);

		logger.info("exception", keyValue("exception", response));

    	return response;
    }

    private String getUri(WebRequest request) {
    	return ((ServletWebRequest)request).getRequest().getRequestURI();
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
