package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;

@Id("on-off")
public interface PlaybackControl<COMMAND extends Command<?, ?>>
        extends Feature {

    public final static String ID = "playback-control";

    public final static String PLAY_COMMAND = "play";
    public final static String PAUSE_COMMAND = "pause";
    public final static String STOP_COMMAND = "stop";
    public final static String FORWARD_COMMAND = "forward";
    public final static String REWIND_COMMAND = "rewind";

    public COMMAND getPlayCommand();
    public COMMAND getPauseCommand();
    public COMMAND getStopCommand();
    public COMMAND getForwardCommand();
    public COMMAND getRewindCommand();
}
