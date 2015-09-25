package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Device;

/**
 * Interface for all devices that allow playback
 * @param <COMMAND> the command type
 */
public interface PlaybackControl<COMMAND extends Command<?, ?, ?, ?>>
        extends Device.Feature {

    String ID = "playback-control";

    /**
     * Get the command for starting playback
     * @return the command for starting playback
     */
    COMMAND getPlayCommand();

    /**
     * Get the command for pausing playback
     * @return the command for pausing playback
     */
    COMMAND getPauseCommand();

    /**
     * Get the command for stopping playback
     * @return the command for stopping playback
     */
    COMMAND getStopCommand();

    /**
     * Get the command for skipping playback forward
     * @return the command for skipping playback forward
     */
    COMMAND getForwardCommand();

    /**
     * Get the command for skipping playback backward
     * @return the command for skipping playback backward
     */
    COMMAND getRewindCommand();
}
