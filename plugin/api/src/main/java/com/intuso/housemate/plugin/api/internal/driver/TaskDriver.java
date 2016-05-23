package com.intuso.housemate.plugin.api.internal.driver;

import org.slf4j.Logger;

/**
 * Created by tomc on 30/09/15.
 */
public interface TaskDriver {

    void start();
    void stop();
    void execute();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends TaskDriver> {
        DRIVER create(Logger logger, Callback callback);
    }
}
