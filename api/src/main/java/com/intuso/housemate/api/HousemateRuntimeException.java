package com.intuso.housemate.api;

/**
 * Exception subclass for all Housemate exceptions so they can be caught separately 
 * @author tclabon
 *
 */
public class HousemateRuntimeException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new exception with a message
	 * @param message Description of what cause the exception
	 */
	public HousemateRuntimeException(String message) {
		super(message);
	}

	/**
	 * Create a new exception with a message and cause
	 * @param message Description of what cause the exception
	 * @param cause another exception that cause this one - used for stack tracing
	 */
	public HousemateRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}