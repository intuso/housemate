package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverBridgeReverse implements HardwareDriver {

    private final com.intuso.housemate.client.api.internal.driver.HardwareDriver hardwareDriver;

    public HardwareDriverBridgeReverse(com.intuso.housemate.client.api.internal.driver.HardwareDriver hardwareDriver) {
        this.hardwareDriver = hardwareDriver;
    }

    public com.intuso.housemate.client.api.internal.driver.HardwareDriver getHardwareDriver() {
        return hardwareDriver;
    }

    @Override
    public void init(Logger logger, Callback callback) {
        hardwareDriver.init(logger, new CallbackBridge(callback));
    }

    @Override
    public void uninit() {
        hardwareDriver.uninit();
    }

    public class CallbackBridge implements com.intuso.housemate.client.api.internal.driver.HardwareDriver.Callback {

        private final Callback callback;

        public CallbackBridge(Callback callback) {
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
    }
}
