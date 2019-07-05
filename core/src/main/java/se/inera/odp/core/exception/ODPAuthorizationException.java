package se.inera.odp.core.exception;

import org.springframework.http.HttpStatus;

public class ODPAuthorizationException extends ODPException {

	private static final long serialVersionUID = 8982527544857381134L;

	public ODPAuthorizationException() {
		super("Behörighet saknas.");
		status = HttpStatus.FORBIDDEN;
	}

	public ODPAuthorizationException(String arg0) {
		super(arg0);
		status = HttpStatus.FORBIDDEN;
	}

	public ODPAuthorizationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		status = HttpStatus.FORBIDDEN;
	}
	
}
