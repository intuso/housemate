package com.intuso.housemate.api;

/**
 * Exception subclass for all Housemate runtime exceptions so they can be caught separately
 */
public class HousemateRuntimeException extends RuntimeException {

	/**
	 * @param message Description of the exception
	 */
	public HousemateRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param message Description of the exception
	 * @param cause another exception that caused this one - used for stack tracing
	 */
	public HousemateRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}