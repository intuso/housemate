package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.object.real.annotations.Value;
import com.intuso.housemate.api.object.device.feature.FeatureId;
import com.intuso.housemate.api.object.device.feature.StatefulPowerControl;

/**
 * Interface to mark real devices that provide stateful power control
 */
@FeatureId("stateful-power-control")
public interface RealStatefulPowerControl extends RealPowerControl {
    public interface Values {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value(id = StatefulPowerControl.IS_ON_VALUE, name = "Is On", description = "True if the device is currently on", typeId = "boolean")
        void isOn(boolean isOn);
    }
}
