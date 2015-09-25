package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Value;

/**
 * Interface for all devices that allow playback with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
public interface StatefulPlaybackControl<COMMAND extends Command<?, ?, ?, ?>, VALUE extends Value<?, ?>>
        extends PlaybackControl<COMMAND> {

    String ID = "stateful-playback-control";

    /**
     * Get the value that describes if the device is currently playing
     * @return the value that describes if the device is currently playing
     */
    VALUE getIsPlayingValue();

    /**
     * is the device currently playing
     * @return true if the device is currently playing
     */
    boolean isPlaying();
}
