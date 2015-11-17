package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.annotations.Value;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@TypeInfo(id = "power-stateful", name = "Power", description = "Power")
public interface RealStatefulPowerControl extends RealPowerControl {

    void setOn(boolean on);

    interface Values {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value("boolean")
        @TypeInfo(id = "is-on", name = "Is On", description = "True if the device is currently on")
        void isOn(boolean isOn);
    }
}
