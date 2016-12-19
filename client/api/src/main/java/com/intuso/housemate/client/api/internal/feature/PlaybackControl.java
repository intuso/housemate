package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.Command;
import com.intuso.housemate.client.api.internal.annotation.Feature;
import com.intuso.housemate.client.api.internal.annotation.Id;

/**
 * Interface to mark real devices that provide playback control
 */
@Feature
@Id(value = "playback", name = "Playback", description = "Playback")
public interface PlaybackControl {

    /**
     * Callback to start playback
     */
    @Command
    @Id(value = "play", name = "Play", description = "Play")
    void play();

    /**
     * Callback to pause playback
     */
    @Command
    @Id(value = "pause", name = "Pause", description = "Pause")
    void pause();

    /**
     * Callback to stop playback
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stopPlayback();

    /**
     * Callback to skip the playback forwards
     */
    @Command
    @Id(value = "forward", name = "Forward", description = "Forward")
    void forward();

    /**
     * Callback to skip the playback backwards
     */
    @Command
    @Id(value = "rewind", name = "Rewind", description = "Rewind")
    void rewind();
}
