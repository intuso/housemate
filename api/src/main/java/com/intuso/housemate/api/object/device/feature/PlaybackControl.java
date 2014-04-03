package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.api.object.command.Command;

/**
 * Interface for all devices that allow playback
 * @param <COMMAND> the command type
 */
@FeatureId("playback-control")
public interface PlaybackControl<COMMAND extends Command<?, ?, ?>>
        extends Feature {

    public final static String ID = "playback-control";

    public final static String PLAY_COMMAND = "play";
    public final static String PAUSE_COMMAND = "pause";
    public final static String STOP_COMMAND = "stop";
    public final static String FORWARD_COMMAND = "forward";
    public final static String REWIND_COMMAND = "rewind";

    /**
     * Get the command for starting playback
     * @return the command for starting playback
     */
    public COMMAND getPlayCommand();

    /**
     * Get the command for pausing playback
     * @return the command for pausing playback
     */
    public COMMAND getPauseCommand();

    /**
     * Get the command for stopping playback
     * @return the command for stopping playback
     */
    public COMMAND getStopCommand();

    /**
     * Get the command for skipping playback forward
     * @return the command for skipping playback forward
     */
    public COMMAND getForwardCommand();

    /**
     * Get the command for skipping playback backward
     * @return the command for skipping playback backward
     */
    public COMMAND getRewindCommand();
}
