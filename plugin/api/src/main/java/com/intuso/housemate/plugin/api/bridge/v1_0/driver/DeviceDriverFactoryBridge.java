package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.api.internal.driver.DeviceDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverFactoryBridge implements DeviceDriver.Factory<DeviceDriver> {

    private final com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?> factory;
    private final DeviceDriverMapper deviceDriverMapper;

    @Inject
    public DeviceDriverFactoryBridge(@Assisted com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?> factory,
                                     DeviceDriverMapper deviceDriverMapper) {
        this.factory = factory;
        this.deviceDriverMapper = deviceDriverMapper;
    }

    public com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public DeviceDriver create(Logger logger, DeviceDriver.Callback callback) {
        return deviceDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Callback {

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
        DeviceDriverFactoryBridge create(com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?> deviceDriverFactory);
    }
}
