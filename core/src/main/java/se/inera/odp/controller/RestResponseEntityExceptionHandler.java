package se.inera.odp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import se.inera.odp.exception.*;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value = { ODPException.class })
    protected ResponseEntity<Object> handleODPException(
      RuntimeException ex, WebRequest request) {
        String bodyOfResponse = message(HttpStatus.NOT_FOUND, ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = { ODPAuthorizationException.class })
    protected ResponseEntity<Object> handleODPAuthorizationException(
      RuntimeException ex, WebRequest request) {
        String bodyOfResponse = message(HttpStatus.FORBIDDEN, "Behörighet saknas.");
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
    
    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    protected ResponseEntity<Object> handleException(
      RuntimeException ex, WebRequest request) {
        String bodyOfResponse = message(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error");
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { HttpClientErrorException.class})
    protected ResponseEntity<Object> handleRestClientException(
    		HttpClientErrorException ex, WebRequest request) {
    	
    	if(ex.getStatusCode().equals(HttpStatus.FORBIDDEN) || 
    			ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
    		return handleODPAuthorizationException(ex, request);
    	else {
	        String bodyOfResponse = message(ex.getStatusCode(), " CKAN response error");
	        return handleExceptionInternal(ex, bodyOfResponse, 
	          new HttpHeaders(), ex.getStatusCode(), request);
    	}
    }

    @ExceptionHandler(value = { RestClientException.class})
    protected ResponseEntity<Object> handleRestClientException(RestClientException ex, WebRequest request) {
    	String bodyOfResponse = message(HttpStatus.INTERNAL_SERVER_ERROR, " Internal server error");
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    private String message(HttpStatus errCode, String msg ) {
    	return String.format("{\n   \"code\" : \"%s\",\n   \"description\" : \"%s\"\n}", errCode, msg);
    }
}
