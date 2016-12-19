package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.Feature;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.housemate.client.api.internal.annotation.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@Id(value = "power-stateful", name = "Power", description = "Power")
public interface StatefulPowerControl extends PowerControl {

    @Values
    interface PowerValues {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value("boolean")
        @Id(value = "is-on", name = "Is On", description = "True if the device is currently on")
        void isOn(boolean isOn);
    }
}
