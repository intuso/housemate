package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverBridge implements DeviceDriver {

    private final com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver deviceDriver;

    public DeviceDriverBridge(com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver deviceDriver) {
        this.deviceDriver = deviceDriver;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver getDeviceDriver() {
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
