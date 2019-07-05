package se.inera.odp.core.exception;

import org.springframework.http.HttpStatus;

public class ODPNotFoundException extends ODPException {

	private static final long serialVersionUID = 8982527544857381134L;

	public ODPNotFoundException() {
		super("Sidan kan inte hittas.");
		status = HttpStatus.NOT_FOUND;
	}

	public ODPNotFoundException(String arg0) {
		super(arg0);
		status = HttpStatus.NOT_FOUND;
	}

	public ODPNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		status = HttpStatus.NOT_FOUND;
	}
	
}
