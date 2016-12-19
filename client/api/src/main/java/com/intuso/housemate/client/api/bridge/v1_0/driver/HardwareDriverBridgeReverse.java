package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;

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
    public void start() {
        hardwareDriver.start();
    }

    @Override
    public void stop() {
        hardwareDriver.stop();
    }
}
