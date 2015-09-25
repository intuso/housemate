package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Command;
import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;
import com.intuso.housemate.object.api.internal.feature.PowerControl;

/**
 * Interface to mark real devices that provide power control
 */
@FeatureId(PowerControl.ID)
public interface RealPowerControl extends RealFeature {

    /**
     * Callback to turn the device on
     */
    @Command(id = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Callback to turn the device off
     */
    @Command(id = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();
}
