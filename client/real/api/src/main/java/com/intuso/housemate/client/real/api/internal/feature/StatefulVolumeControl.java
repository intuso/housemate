package com.intuso.housemate.client.real.api.internal.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.Id;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful volume control
 */
@Feature
@Id(value = "volume-stateful", name = "Volume", description = "Volume")
public interface StatefulVolumeControl extends VolumeControl {

    @Values
    interface VolumeValues {

        /**
         * Callback for when the current volume has changed
         * @param currentVolume the new current volume
         */
        @Value("integer")
        @Id(value = "current-volume", name = "Current Volume", description = "The device's current volume")
        void currentVolume(int currentVolume);
    }
}
