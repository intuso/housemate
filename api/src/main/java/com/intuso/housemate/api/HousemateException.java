package com.intuso.housemate.api;

/**
 * Exception subclass for all Housemate exceptions so they can be caught separately
 */
public class HousemateException extends Exception {

	/**
	 * @param message Description of the exception
	 */
	public HousemateException(String message) {
		super(message);
	}
	
	/**
	 * @param message Description of the exception
	 * @param cause another exception that caused this one - used for stack tracing
	 */
	public HousemateException(String message, Throwable cause) {
		super(message, cause);
	}
}