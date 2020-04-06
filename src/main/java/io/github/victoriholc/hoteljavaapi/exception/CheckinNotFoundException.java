package io.github.victoriholc.hoteljavaapi.exception;

public class CheckinNotFoundException extends Exception {

	public CheckinNotFoundException() {
		super();
	}

	public CheckinNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CheckinNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CheckinNotFoundException(String message) {
		super(message);
	}

	public CheckinNotFoundException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
