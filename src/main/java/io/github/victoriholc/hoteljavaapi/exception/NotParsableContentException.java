package io.github.victoriholc.hoteljavaapi.exception;


/**
 * NotParsableContentException in the API
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
public class NotParsableContentException extends Exception {

	private static final long serialVersionUID = -2755948314876865552L;

	public NotParsableContentException() {
	}

	public NotParsableContentException(String message) {
		super(message);
	}

	public NotParsableContentException(Throwable cause) {
		super(cause);
	}

	public NotParsableContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotParsableContentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
