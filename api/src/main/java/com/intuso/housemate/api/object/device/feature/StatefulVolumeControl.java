package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * Interface for all devices that allow volume control with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
@FeatureId("stateful-volume-control")
public interface StatefulVolumeControl<COMMAND extends Command<?, ?, ?>, VALUE extends Value<?, ?>>
        extends VolumeControl<COMMAND> {

    public final static String ID = "stateful-volume-control";

    public final static String CURRENT_VOLUME_VALUE = "current-volume";

    /**
     * Get the value for the current volume
     * @return the value for the current volume
     */
    public VALUE getCurrentVolumeValue();
}
