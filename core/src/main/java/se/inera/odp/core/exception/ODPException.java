package se.inera.odp.core.exception;

import org.springframework.http.HttpStatus;

public class ODPException extends RuntimeException {

	private static final long serialVersionUID = -6084940960373498988L;
	
	HttpStatus status = HttpStatus.NOT_FOUND;
	private final static String msg = "Ett fel har uppstått!";

	public ODPException() {
		super(msg);
	}

	public ODPException(String arg0) {
		super(arg0);
	}

	public ODPException(HttpStatus status, String arg0) {
		super(arg0);
		this.status=status;
	}

	public ODPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HttpStatus getStatus() {
		return status;
	}
	
}
