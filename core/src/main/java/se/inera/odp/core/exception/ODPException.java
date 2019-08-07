package se.inera.odp.core.exception;

import org.springframework.http.HttpStatus;

public class ODPException extends RuntimeException {

	private static final long serialVersionUID = -6084940960373498988L;
	
	private String errCode = null;
	HttpStatus status = HttpStatus.NOT_FOUND;
	private final static String MSG = "Ett fel har uppstått!";

	public ODPException() {
		super(MSG);
	}

	public ODPException(String msg) {
		super(msg);
	}

	public ODPException(HttpStatus status, String msg) {
		super(msg);
		this.status=status;
	}

	public ODPException(HttpStatus status, String msg, String errCode) {
		super(msg);
		this.status=status;
		this.errCode = errCode;
	}

	public ODPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}	
}
