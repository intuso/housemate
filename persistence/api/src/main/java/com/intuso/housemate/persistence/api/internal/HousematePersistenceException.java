package com.intuso.housemate.persistence.api.internal;

/**
 */
public class HousematePersistenceException extends RuntimeException {
    public HousematePersistenceException() {
    }

    public HousematePersistenceException(String message) {
        super(message);
    }

    public HousematePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HousematePersistenceException(Throwable cause) {
        super(cause);
    }
}
