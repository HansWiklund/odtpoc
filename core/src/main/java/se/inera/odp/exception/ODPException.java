package se.inera.odp.exception;

public class ODPException extends RuntimeException {

	private static final long serialVersionUID = -6084940960373498988L;

	public ODPException(String arg0) {
		super(arg0);
	}

	public ODPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
}
