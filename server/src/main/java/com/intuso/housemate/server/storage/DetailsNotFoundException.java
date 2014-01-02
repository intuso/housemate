package com.intuso.housemate.server.storage;

/**
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
