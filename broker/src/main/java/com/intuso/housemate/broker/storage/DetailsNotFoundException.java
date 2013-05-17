package com.intuso.housemate.broker.storage;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 01/03/13
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
public class DetailsNotFoundException extends Exception {
    public DetailsNotFoundException() {
    }

    public DetailsNotFoundException(String message) {
        super(message);
    }

    public DetailsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DetailsNotFoundException(Throwable cause) {
        super(cause);
    }
}
