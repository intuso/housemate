package com.intuso.housemate.comms.api.internal;

/**
 * Created by tomc on 14/09/15.
 */
public class HousemateCommsException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public HousemateCommsException() {}

    public HousemateCommsException(String s) {
        super(s);
    }

    public HousemateCommsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HousemateCommsException(Throwable throwable) {
        super(throwable);
    }
}
