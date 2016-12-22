package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.api.internal.driver.FeatureDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverBridgeReverse implements FeatureDriver {

    private final com.intuso.housemate.client.api.internal.driver.FeatureDriver featureDriver;

    public FeatureDriverBridgeReverse(com.intuso.housemate.client.api.internal.driver.FeatureDriver featureDriver) {
        this.featureDriver = featureDriver;
    }

    public com.intuso.housemate.client.api.internal.driver.FeatureDriver getFeatureDriver() {
        return featureDriver;
    }

    @Override
    public void startFeature() {
        featureDriver.startFeature();
    }

    @Override
    public void stopFeature() {
        featureDriver.stopFeature();
    }
}
