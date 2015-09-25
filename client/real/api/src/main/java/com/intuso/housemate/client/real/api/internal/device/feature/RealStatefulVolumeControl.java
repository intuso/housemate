package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.object.api.internal.feature.StatefulVolumeControl;

/**
 * Interface to mark real devices that provide stateful volume control
 */
@FeatureId(StatefulVolumeControl.ID)
public interface RealStatefulVolumeControl extends RealVolumeControl {
    public interface Values {

        /**
         * Callback for when the current volume has changed
         * @param currentVolume the new current volume
         */
        @Value(id = "current-volume", name = "Current Volume", description = "The device's current volume", typeId = "integer")
        void currentVolume(int currentVolume);
    }
}
