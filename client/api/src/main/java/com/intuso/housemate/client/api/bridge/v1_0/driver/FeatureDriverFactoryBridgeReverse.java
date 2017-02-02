package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverFactoryBridgeReverse implements FeatureDriver.Factory<FeatureDriver> {

    private final com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<?> factory;
    private final FeatureDriverMapper featureDriverMapper;

    @Inject
    public FeatureDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<?> factory,
                                             FeatureDriverMapper featureDriverMapper) {
        this.factory = factory;
        this.featureDriverMapper = featureDriverMapper;
    }

    public com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public FeatureDriver create(Logger logger, FeatureDriver.Callback callback) {
        return featureDriverMapper.map(factory.create(logger, new FeatureDriverBridgeReverse.CallbackBridge(callback)));
    }

    public interface Factory {
        FeatureDriverFactoryBridgeReverse create(com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<?> featureDriverFactory);
    }
}
