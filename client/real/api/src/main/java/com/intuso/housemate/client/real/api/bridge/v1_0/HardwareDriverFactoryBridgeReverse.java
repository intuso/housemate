package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverFactoryBridgeReverse implements HardwareDriver.Factory<HardwareDriver> {

    private final com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<?> factory;
    private final HardwareDriverMapper hardwareDriverMapper;

    @Inject
    public HardwareDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<?> factory,
                                              HardwareDriverMapper hardwareDriverMapper) {
        this.factory = factory;
        this.hardwareDriverMapper = hardwareDriverMapper;
    }

    public com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public HardwareDriver create(HardwareDriver.Callback callback) {
        return hardwareDriverMapper.map(factory.create(new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Callback {

        private final HardwareDriver.Callback callback;

        private CallbackBridge(HardwareDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        HardwareDriverFactoryBridgeReverse create(com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<?> hardwareDriverFactory);
    }
}
