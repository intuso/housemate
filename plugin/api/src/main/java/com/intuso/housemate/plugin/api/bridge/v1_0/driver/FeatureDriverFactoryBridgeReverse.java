package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class FeatureDriverFactoryBridgeReverse implements FeatureDriver.Factory<FeatureDriver> {

    private final com.intuso.housemate.plugin.api.internal.driver.FeatureDriver.Factory<?> factory;
    private final FeatureDriverMapper featureDriverMapper;

    @Inject
    public FeatureDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.plugin.api.internal.driver.FeatureDriver.Factory<?> factory,
                                             FeatureDriverMapper featureDriverMapper) {
        this.factory = factory;
        this.featureDriverMapper = featureDriverMapper;
    }

    public com.intuso.housemate.plugin.api.internal.driver.FeatureDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public FeatureDriver create(Logger logger, FeatureDriver.Callback callback) {
        return featureDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.plugin.api.internal.driver.FeatureDriver.Callback {

        private final FeatureDriver.Callback callback;

        private CallbackBridge(FeatureDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        FeatureDriverFactoryBridgeReverse create(com.intuso.housemate.plugin.api.internal.driver.FeatureDriver.Factory<?> featureDriverFactory);
    }
}
