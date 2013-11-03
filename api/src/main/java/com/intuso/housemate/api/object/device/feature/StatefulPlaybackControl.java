package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

@Id("stateful-playback")
public interface StatefulPlaybackControl<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends PlaybackControl<COMMAND> {

    public final static String ID = "stateful-playback-control";

    public final static String IS_PLAYING_VALUE = "is-playing";

    public VALUE getIsPlayingValue();
    public boolean isPlaying();
}
