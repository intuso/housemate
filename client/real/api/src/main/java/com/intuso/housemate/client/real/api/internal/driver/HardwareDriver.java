package com.intuso.housemate.client.real.api.internal.driver;

import org.slf4j.Logger;

/**
 * Created by tomc on 30/09/15.
 */
public interface HardwareDriver {

    void start();
    void stop();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends HardwareDriver> {
        DRIVER create(Logger logger, Callback callback);
    }
}
