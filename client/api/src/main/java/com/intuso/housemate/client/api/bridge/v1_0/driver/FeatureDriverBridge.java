package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;

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
    public void start() {
        featureDriver.start();
    }

    @Override
    public void stop() {
        featureDriver.stop();
    }
}
