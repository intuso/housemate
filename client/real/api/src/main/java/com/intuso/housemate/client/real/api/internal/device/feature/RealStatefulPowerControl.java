package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.object.api.internal.feature.StatefulPowerControl;

/**
 * Interface to mark real devices that provide stateful power control
 */
@FeatureId(StatefulPowerControl.ID)
public interface RealStatefulPowerControl extends RealPowerControl {
    interface Values {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value(id = "is-on", name = "Is On", description = "True if the device is currently on", typeId = "boolean")
        void isOn(boolean isOn);
    }
}
