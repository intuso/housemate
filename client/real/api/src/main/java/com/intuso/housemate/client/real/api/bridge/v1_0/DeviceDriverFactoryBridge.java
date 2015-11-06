package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverFactoryBridge implements DeviceDriver.Factory<DeviceDriver> {

    private final com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<?> factory;
    private final DeviceDriverMapper deviceDriverMapper;

    @Inject
    public DeviceDriverFactoryBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<?> factory,
                                     DeviceDriverMapper deviceDriverMapper) {
        this.factory = factory;
        this.deviceDriverMapper = deviceDriverMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public DeviceDriver create(DeviceDriver.Callback callback) {
        return deviceDriverMapper.map(factory.create(new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Callback {

        private final DeviceDriver.Callback callback;

        private CallbackBridge(DeviceDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        DeviceDriverFactoryBridge create(com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<?> deviceDriverFactory);
    }
}
