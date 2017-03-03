package com.intuso.housemate.client.serialisation.api.internal;

/**
 * Created by tomc on 21/02/17.
 */
public class SerialisationException extends RuntimeException {

    public SerialisationException() {}

    public SerialisationException(String s) {
        super(s);
    }

    public SerialisationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SerialisationException(Throwable throwable) {
        super(throwable);
    }
}
