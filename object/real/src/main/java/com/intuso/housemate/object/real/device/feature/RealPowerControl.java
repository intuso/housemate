package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.object.real.annotations.Command;
import com.intuso.housemate.api.object.device.feature.FeatureId;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.feature.PowerControl;

/**
 * Interface to mark real devices that provide power control
 */
@FeatureId("power-control")
public interface RealPowerControl extends RealFeature {

    /**
     * Callback to turn the device on
     * @throws HousemateException
     */
    @Command(id = PowerControl.ON_COMMAND, name = "Turn On", description = "Turn the device on")
    void turnOn() throws HousemateException;

    /**
     * Callback to turn the device off
     * @throws HousemateException
     */
    @Command(id = PowerControl.OFF_COMMAND, name = "Turn Off", description = "Turn the device off")
    void turnOff() throws HousemateException;
}
