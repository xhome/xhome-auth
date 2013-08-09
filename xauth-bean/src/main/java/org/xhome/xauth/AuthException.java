package org.xhome.xauth;

/**
 * @project xauth-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 3, 201311:13:28 PM
 */
public class AuthException extends Exception {
	
	private static final long	serialVersionUID	= 5286273561797618517L;
	private short status;
	
	public AuthException() {
		super();
	}
	
	public AuthException(short status) {
		super();
		this.setStatus(status);
	}
	
	public AuthException(String message) {
		super(message);
	}
	
	public AuthException(short status, String message) {
		super(message);
		this.setStatus(status);
	}
	
	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AuthException(short status, String message, Throwable cause) {
		super(message, cause);
		this.setStatus(status);
	}
	
	public AuthException(Throwable cause) {
		super(cause);
	}
	
	public AuthException(short status, Throwable cause) {
		super(cause);
		this.setStatus(status);
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public short getStatus() {
		return status;
	}
	
}
