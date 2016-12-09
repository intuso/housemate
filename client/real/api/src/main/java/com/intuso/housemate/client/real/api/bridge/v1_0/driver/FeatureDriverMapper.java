package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver, FeatureDriver> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver, FeatureDriver>() {
        @Override
        public FeatureDriver apply(com.intuso.housemate.client.real.api.internal.driver.FeatureDriver featureDriver) {
            return map(featureDriver);
        }
    };

    private final Function<FeatureDriver, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver> fromV1_0Function = new Function<FeatureDriver, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.driver.FeatureDriver apply(FeatureDriver featureDriver) {
            return map(featureDriver);
        }
    };

    public Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver, FeatureDriver> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<FeatureDriver, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.FeatureDriver, TO extends FeatureDriver>
        TO map(FROM featureDriver) {
        if(featureDriver == null)
            return null;
        else if(featureDriver instanceof FeatureDriverBridge)
            return (TO) ((FeatureDriverBridge) featureDriver).getFeatureDriver();
        return (TO) new FeatureDriverBridgeReverse(featureDriver);
    }

    public <FROM extends FeatureDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.FeatureDriver>
        TO map(FROM featureDriver) {
        if(featureDriver == null)
            return null;
        else if(featureDriver instanceof FeatureDriverBridgeReverse)
            return (TO) ((FeatureDriverBridgeReverse) featureDriver).getFeatureDriver();
        return (TO) new FeatureDriverBridge(featureDriver);
    }
}
