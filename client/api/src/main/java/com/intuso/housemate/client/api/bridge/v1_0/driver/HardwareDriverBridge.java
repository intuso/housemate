package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverBridge implements com.intuso.housemate.client.api.internal.driver.HardwareDriver {

    private final HardwareDriver hardwareDriver;

    public HardwareDriverBridge(HardwareDriver hardwareDriver) {
        this.hardwareDriver = hardwareDriver;
    }

    public HardwareDriver getHardwareDriver() {
        return hardwareDriver;
    }

    @Override
    public void startHardware() {
        hardwareDriver.startHardware();
    }

    @Override
    public void stopHardware() {
        hardwareDriver.stopHardware();
    }
}
