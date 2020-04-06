package io.github.victoriholc.hoteljavaapi.exception;

/**
 * TransactionInvalidUpdateException in the API.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
public class GuestInvalidUpdateException extends Exception {

	private static final long serialVersionUID = -1348320851641234558L;
	
	public GuestInvalidUpdateException() {
		super();
	}

	public GuestInvalidUpdateException(String message) {
		super(message);
	}
	
	public GuestInvalidUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GuestInvalidUpdateException(String message, Throwable cause, 
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
