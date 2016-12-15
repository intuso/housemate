package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.intuso.housemate.plugin.api.internal.driver.HardwareDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverBridge implements HardwareDriver {

    private final com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver hardwareDriver;

    public HardwareDriverBridge(com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver hardwareDriver) {
        this.hardwareDriver = hardwareDriver;
    }

    public com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver getHardwareDriver() {
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
