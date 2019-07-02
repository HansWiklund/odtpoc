package se.inera.odp.core.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
 
    @ExceptionHandler(value = { ODPException.class })
    protected ResponseEntity<Object> handleODPException(RuntimeException ex, WebRequest request) {
    	Map<String, Object>  bodyOfResponse = message(HttpStatus.NOT_FOUND, ex.getMessage(), getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = { ODPAuthorizationException.class })
    protected ResponseEntity<Object> handleODPAuthorizationException(RuntimeException ex, WebRequest request) {
        Map<String, Object> bodyOfResponse = message(HttpStatus.FORBIDDEN, "Behörighet saknas.", getUri(request));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
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

    private Map<String, Object> message(HttpStatus errCode, String msg, String url ) {
    	
    	LocalDateTime localDate = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", localDate);
        response.put("status", errCode.value());
        response.put("error", errCode.getReasonPhrase());
        response.put("message", msg);
        response.put("path", url);

    	return response;
    }

    private String message(HttpStatus errCode, String msg ) {

    	return String.format("{\n   \"code\" : \"%s\",\n   \"description\" : \"%s\"\n}", errCode, msg);
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
