package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverBridgeReverse implements DeviceDriver {

    private final com.intuso.housemate.plugin.api.internal.driver.DeviceDriver deviceDriver;

    public DeviceDriverBridgeReverse(com.intuso.housemate.plugin.api.internal.driver.DeviceDriver deviceDriver) {
        this.deviceDriver = deviceDriver;
    }

    public com.intuso.housemate.plugin.api.internal.driver.DeviceDriver getDeviceDriver() {
        return deviceDriver;
    }

    @Override
    public void start() {
        deviceDriver.start();
    }

    @Override
    public void stop() {
        deviceDriver.stop();
    }
}
