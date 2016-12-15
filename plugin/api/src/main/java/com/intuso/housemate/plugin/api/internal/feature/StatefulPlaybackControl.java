package com.intuso.housemate.plugin.api.internal.feature;

import com.intuso.housemate.plugin.api.internal.annotations.Feature;
import com.intuso.housemate.plugin.api.internal.annotations.Id;
import com.intuso.housemate.plugin.api.internal.annotations.Value;
import com.intuso.housemate.plugin.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@Feature
@Id(value = "playback-stateful", name = "Playback", description = "Playback")
public interface StatefulPlaybackControl extends PlaybackControl {

    @Values
    interface PlaybackValues {

        /**
         * Callback to set the current playing value
         * @param isPlaying true if the device is currently playing
         */
        @Value("boolean")
        @Id(value = "is-playing", name = "Is Playing", description = "True if the device is currently playing")
        void isPlaying(boolean isPlaying);
    }
}
