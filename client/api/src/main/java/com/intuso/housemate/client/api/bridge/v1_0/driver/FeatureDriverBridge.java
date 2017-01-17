package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverBridge implements com.intuso.housemate.client.api.internal.driver.FeatureDriver {

    private final FeatureDriver featureDriver;

    public FeatureDriverBridge(FeatureDriver featureDriver) {
        this.featureDriver = featureDriver;
    }

    public FeatureDriver getFeatureDriver() {
        return featureDriver;
    }

    @Override
    public void init(Logger logger, Callback callback) {
        featureDriver.init(logger, new CallbackBridge(callback));
    }

    @Override
    public void uninit() {
        featureDriver.uninit();
    }

    public class CallbackBridge implements FeatureDriver.Callback {

        private final Callback callback;

        public CallbackBridge(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }
}
