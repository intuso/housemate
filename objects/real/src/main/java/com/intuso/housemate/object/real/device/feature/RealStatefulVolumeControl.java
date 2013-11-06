package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.device.feature.StatefulVolumeControl;

/**
 * Interface to mark real devices that provide stateful volume control
 */
@Id("stateful-volume-control")
public interface RealStatefulVolumeControl extends RealVolumeControl {
    public interface Values {

        /**
         * Callback for when the current volume has changed
         * @param currentVolume the new current volume
         */
        @Value(id = StatefulVolumeControl.CURRENT_VOLUME_VALUE, name = "Current Volume", description = "THe device's current volume", typeId = "integer")
        void currentVolume(int currentVolume);
    }
}
