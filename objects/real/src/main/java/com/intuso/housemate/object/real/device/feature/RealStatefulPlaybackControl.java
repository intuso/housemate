package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@Id("stateful-playback-control")
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
