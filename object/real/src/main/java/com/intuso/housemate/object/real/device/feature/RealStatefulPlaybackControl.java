package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.object.real.annotations.Value;
import com.intuso.housemate.api.object.device.feature.FeatureId;
import com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@FeatureId("stateful-playback-control")
public interface RealStatefulPlaybackControl extends RealPlaybackControl {
    public interface Values {

        /**
         * Callback to set the current playing value
         * @param isPlaying true if the device is currently playing
         */
        @Value(id = StatefulPlaybackControl.IS_PLAYING_VALUE, name = "Is Playing", description = "True if the device is currently playing", typeId = "boolean")
        void isPlaying(boolean isPlaying);
    }
}
