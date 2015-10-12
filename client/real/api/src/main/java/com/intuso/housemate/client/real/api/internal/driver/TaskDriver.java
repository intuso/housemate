package com.intuso.housemate.client.real.api.internal.driver;

/**
 * Created by tomc on 30/09/15.
 */
public interface TaskDriver {

    void execute();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends TaskDriver> {
        DRIVER create(Callback callback);
    }
}
