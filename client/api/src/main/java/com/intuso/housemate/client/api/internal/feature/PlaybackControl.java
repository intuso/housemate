package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * API for controlling playback
 */
@Feature
@Id(value = "playback", name = "Playback", description = "Playback")
public interface PlaybackControl {

    String ID = PlaybackControl.class.getAnnotation(Id.class).value();

    /**
     * Play
     */
    @Command
    @Id(value = "play", name = "Play", description = "Play")
    void play();

    /**
     * Pause
     */
    @Command
    @Id(value = "pause", name = "Pause", description = "Pause")
    void pause();

    /**
     * Stop
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stopPlayback();

    /**
     * Forward
     */
    @Command
    @Id(value = "forward", name = "Forward", description = "Forward")
    void forward();

    /**
     * Rewind
     */
    @Command
    @Id(value = "rewind", name = "Rewind", description = "Rewind")
    void rewind();

    /**
     * API for controlling playback with state
     */
    @Feature
    @Id(value = "playback-stateful", name = "Playback", description = "Playback")
    interface Stateful extends PlaybackControl {

        String ID = Stateful.class.getAnnotation(Id.class).value();

        /**
         * Get whether the device is currently playing
         * @return true if the device is currently playing
         */
        @Value("boolean")
        @Id(value = "playing", name = "Playing", description = "True if the device is currently playing")
        boolean isPlaying();

        /**
         * Add a listener
         */
        @AddListener
        ListenerRegistration addListener(Listener listener);
    }

    interface Listener {

        /**
         * Callback for when playback starts or stops
         * @param playing true if the device is now playing
         */
        @Value("boolean")
        @Id(value = "playing", name = "Playing", description = "True if the device is currently playing")
        void playing(boolean playing);
    }
}
