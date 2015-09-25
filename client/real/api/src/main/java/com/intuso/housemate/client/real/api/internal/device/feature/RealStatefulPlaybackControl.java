package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.object.api.internal.feature.StatefulPlaybackControl;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@FeatureId(StatefulPlaybackControl.ID)
public interface RealStatefulPlaybackControl extends RealPlaybackControl {
    public interface Values {

        /**
         * Callback to set the current playing value
         * @param isPlaying true if the device is currently playing
         */
        @Value(id = "is-playing", name = "Is Playing", description = "True if the device is currently playing", typeId = "boolean")
        void isPlaying(boolean isPlaying);
    }
}
