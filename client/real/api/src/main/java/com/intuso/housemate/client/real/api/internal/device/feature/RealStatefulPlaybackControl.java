package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.annotations.Value;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@Feature
@TypeInfo(id = "playback-stateful", name = "Playback", description = "Playback")
public interface RealStatefulPlaybackControl extends RealPlaybackControl {

    interface Values {

        /**
         * Callback to set the current playing value
         * @param isPlaying true if the device is currently playing
         */
        @Value("boolean")
        @TypeInfo(id = "is-playing", name = "Is Playing", description = "True if the device is currently playing")
        void isPlaying(boolean isPlaying);
    }
}
