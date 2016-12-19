package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverFactoryBridge implements com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<com.intuso.housemate.client.api.internal.driver.HardwareDriver> {

    private final HardwareDriver.Factory<?> factory;
    private final HardwareDriverMapper hardwareDriverMapper;

    @Inject
    public HardwareDriverFactoryBridge(@Assisted HardwareDriver.Factory<?> factory,
                                       HardwareDriverMapper hardwareDriverMapper) {
        this.factory = factory;
        this.hardwareDriverMapper = hardwareDriverMapper;
    }

    public HardwareDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public com.intuso.housemate.client.api.internal.driver.HardwareDriver create(Logger logger, com.intuso.housemate.client.api.internal.driver.HardwareDriver.Callback callback) {
        return hardwareDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements HardwareDriver.Callback {

        private final com.intuso.housemate.client.api.internal.driver.HardwareDriver.Callback callback;

        private CallbackBridge(com.intuso.housemate.client.api.internal.driver.HardwareDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        HardwareDriverFactoryBridge create(HardwareDriver.Factory<?> hardwareDriverFactory);
    }
}
