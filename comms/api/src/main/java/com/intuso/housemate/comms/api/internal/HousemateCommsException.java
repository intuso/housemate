package com.intuso.housemate.comms.api.internal;

/**
 * Created by tomc on 14/09/15.
 */
public class HousemateCommsException extends RuntimeException {
    public HousemateCommsException() {
    }

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
