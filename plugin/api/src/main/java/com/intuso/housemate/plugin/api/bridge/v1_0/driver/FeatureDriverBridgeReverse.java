package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverBridgeReverse implements FeatureDriver {

    private final com.intuso.housemate.plugin.api.internal.driver.FeatureDriver featureDriver;

    public FeatureDriverBridgeReverse(com.intuso.housemate.plugin.api.internal.driver.FeatureDriver featureDriver) {
        this.featureDriver = featureDriver;
    }

    public com.intuso.housemate.plugin.api.internal.driver.FeatureDriver getFeatureDriver() {
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
