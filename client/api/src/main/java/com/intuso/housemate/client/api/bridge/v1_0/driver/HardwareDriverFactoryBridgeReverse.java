package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverFactoryBridgeReverse implements HardwareDriver.Factory<HardwareDriver> {

    private final com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<?> factory;
    private final HardwareDriverMapper hardwareDriverMapper;

    @Inject
    public HardwareDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<?> factory,
                                              HardwareDriverMapper hardwareDriverMapper) {
        this.factory = factory;
        this.hardwareDriverMapper = hardwareDriverMapper;
    }

    public com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public HardwareDriver create(Logger logger, HardwareDriver.Callback callback) {
        return hardwareDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.api.internal.driver.HardwareDriver.Callback {

        private final HardwareDriver.Callback callback;

        private CallbackBridge(HardwareDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }

        @Override
        public void addObject(Object object, String prefix) {
            callback.addObject(object, prefix);
        }

        @Override
        public void removeObject(Object object) {
            callback.removeObject(object);
        }
    }

    public interface Factory {
        HardwareDriverFactoryBridgeReverse create(com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<?> hardwareDriverFactory);
    }
}
