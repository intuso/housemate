package com.intuso.housemate.client.real.api.internal.impl.device;

import com.intuso.housemate.client.real.api.internal.device.feature.RealStatefulPowerControl;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class StatefulPoweredDevice implements DeviceDriver, RealStatefulPowerControl {

    @com.intuso.housemate.client.v1_0.real.api.annotations.Values
    public Values values;

    /**
     * Sets the device to be on
     */
    public final void setOn() {
        values.isOn(true);
    }

    /**
     * Sets the device to be off
     */
    public final void setOff() {
        values.isOn(false);
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}
}
