package io.github.victoriholc.hoteljavaapi.exception;

/**
 * TransactionNotFoundException in the API
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
public class GuestNotFoundException extends Exception {

	private static final long serialVersionUID = 7961868979185393122L;

	public GuestNotFoundException() {
	}

	public GuestNotFoundException(String message) {
		super(message);
	}

	public GuestNotFoundException(Throwable cause) {
		super(cause);
	}

	public GuestNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuestNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
