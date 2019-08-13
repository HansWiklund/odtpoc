package se.inera.odp.core.utils;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Component
public class ResponseLoggerMapper {

	Logger logger = LoggerFactory.getLogger(ResponseLoggerMapper.class);

    public Map<String, Object> errorMessage(HttpStatus status, String msg, String url ) {
    	return errorMessage(status, msg, url, null);
    }
    
    public Map<String, Object> errorMessage(HttpStatus status, String msg, String url, String errCode ) {
    	
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

    public Map<String, Object> responseMessage(HttpStatus status, String msg, String url ) {
    	
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", msg);
        response.put("path", url);

		logger.info("response", keyValue("response", response));

    	return response;
    }

    public String getUri(WebRequest request) {
    	return ((ServletWebRequest)request).getRequest().getRequestURI();
    }
}
