package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverFactoryBridge implements com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<com.intuso.housemate.client.api.internal.driver.FeatureDriver> {

    private final FeatureDriver.Factory<?> factory;
    private final FeatureDriverMapper featureDriverMapper;

    @Inject
    public FeatureDriverFactoryBridge(@Assisted FeatureDriver.Factory<?> factory,
                                      FeatureDriverMapper featureDriverMapper) {
        this.factory = factory;
        this.featureDriverMapper = featureDriverMapper;
    }

    public FeatureDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public com.intuso.housemate.client.api.internal.driver.FeatureDriver create(Logger logger, com.intuso.housemate.client.api.internal.driver.FeatureDriver.Callback callback) {
        return featureDriverMapper.map(factory.create(logger, new FeatureDriverBridge.CallbackBridge(callback)));
    }

    public interface Factory {
        FeatureDriverFactoryBridge create(FeatureDriver.Factory<?> featureDriverFactory);
    }
}
