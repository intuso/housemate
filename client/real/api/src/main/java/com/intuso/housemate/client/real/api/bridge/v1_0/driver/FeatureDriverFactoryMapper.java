package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverFactoryMapper {

    private Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>, FeatureDriver.Factory<?>> toFunction = new Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>, FeatureDriver.Factory<?>>() {
        @Override
        public FeatureDriver.Factory<?> apply(com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?> featureDriverFactory) {
            return map(featureDriverFactory);
        }
    };

    private final Function<FeatureDriver.Factory<?>, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>> fromFunction = new Function<FeatureDriver.Factory<?>, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?> apply(FeatureDriver.Factory<?> featureDriverFactory) {
            return map(featureDriverFactory);
        }
    };

    private final FeatureDriverFactoryBridge.Factory bridgeFactory;
    private final FeatureDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public FeatureDriverFactoryMapper(FeatureDriverFactoryBridge.Factory bridgeFactory, FeatureDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>, FeatureDriver.Factory<?>> getToV1_0Function() {
        return toFunction;
    }

    public Function<FeatureDriver.Factory<?>, com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?>> getFromV1_0Function() {
        return fromFunction;
    }

    public FeatureDriver.Factory<?> map(com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?> featureDriverFactory) {
        if(featureDriverFactory == null)
            return null;
        else if(featureDriverFactory instanceof FeatureDriverFactoryBridge)
            return ((FeatureDriverFactoryBridge)featureDriverFactory).getFactory();
        return reverseBridgeFactory.create(featureDriverFactory);
    }

    public com.intuso.housemate.client.real.api.internal.driver.FeatureDriver.Factory<?> map(FeatureDriver.Factory<?> featureDriverFactory) {
        if(featureDriverFactory == null)
            return null;
        else if(featureDriverFactory instanceof FeatureDriverFactoryBridgeReverse)
            return ((FeatureDriverFactoryBridgeReverse)featureDriverFactory).getFactory();
        return bridgeFactory.create(featureDriverFactory);
    }
}
