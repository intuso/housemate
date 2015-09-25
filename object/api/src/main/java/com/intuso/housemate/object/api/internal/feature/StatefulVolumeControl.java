package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Value;

/**
 * Interface for all devices that allow volume control with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
public interface StatefulVolumeControl<COMMAND extends Command<?, ?, ?, ?>, VALUE extends Value<?, ?>>
        extends VolumeControl<COMMAND> {

    String ID = "stateful-volume-control";

    /**
     * Get the value for the current volume
     * @return the value for the current volume
     */
    VALUE getCurrentVolumeValue();
}
