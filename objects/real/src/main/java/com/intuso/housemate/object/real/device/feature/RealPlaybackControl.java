package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;

/**
 * Interface to mark real devices that provide playback control
 */
@Id("playback-control")
public interface RealPlaybackControl extends RealFeature {

    /**
     * Callback to start playback
     * @throws HousemateException
     */
    @Command(id = "play", name = "Play", description = "Play")
    void play() throws HousemateException;

    /**
     * Callback to pause playback
     * @throws HousemateException
     */
    @Command(id = "pause", name = "Pause", description = "Pause")
    void pause() throws HousemateException;

    /**
     * Callback to stop playback
     * @throws HousemateException
     */
    @Command(id = "stop", name = "Stop", description = "Stop")
    void stopPlayback() throws HousemateException;

    /**
     * Callback to skip the playback forwards
     * @throws HousemateException
     */
    @Command(id = "forward", name = "Forward", description = "Forward")
    void forward() throws HousemateException;

    /**
     * Callback to skip the playback backwards
     * @throws HousemateException
     */
    @Command(id = "rewind", name = "Rewind", description = "Rewind")
    void rewind() throws HousemateException;
}
