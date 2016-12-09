package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverBridgeReverse implements FeatureDriver {

    private final com.intuso.housemate.client.real.api.internal.driver.FeatureDriver featureDriver;

    public FeatureDriverBridgeReverse(com.intuso.housemate.client.real.api.internal.driver.FeatureDriver featureDriver) {
        this.featureDriver = featureDriver;
    }

    public com.intuso.housemate.client.real.api.internal.driver.FeatureDriver getFeatureDriver() {
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
