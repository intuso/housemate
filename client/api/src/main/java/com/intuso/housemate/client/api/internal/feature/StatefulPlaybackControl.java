package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.Feature;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.housemate.client.api.internal.annotation.Values;

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
