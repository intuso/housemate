package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * Interface for all devices that allow playback with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
@Id("stateful-playback")
public interface StatefulPlaybackControl<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends PlaybackControl<COMMAND> {

    public final static String ID = "stateful-playback-control";

    public final static String IS_PLAYING_VALUE = "is-playing";

    /**
     * Get the value that describes if the device is currently playing
     * @return the value that describes if the device is currently playing
     */
    public VALUE getIsPlayingValue();

    /**
     * is the device currently playing
     * @return true if the device is currently playing
     */
    public boolean isPlaying();
}
