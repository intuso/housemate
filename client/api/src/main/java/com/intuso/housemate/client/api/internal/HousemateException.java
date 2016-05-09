package com.intuso.housemate.client.api.internal;

/**
 * Created by tomc on 14/09/15.
 */
public class HousemateException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public HousemateException() {}

    public HousemateException(String s) {
        super(s);
    }

    public HousemateException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HousemateException(Throwable throwable) {
        super(throwable);
    }
}
