package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Device;

/**
 * Interface for all devices that allow volume control
 * @param <COMMAND> the command type
 */
public interface VolumeControl<COMMAND extends Command<?, ?, ?, ?>>
        extends Device.Feature {

    String ID = "volume-control";

    /**
     * Get the command to mute the volume
     * @return the command to mute  the volume
     */
    COMMAND getMuteCommand();

    /**
     * Get the command to increase the volume
     * @return the command to increase the volume
     */
    COMMAND getVolumeUpCommand();

    /**
     * Get the command to decrease the volume
     * @return the command to decrease the volume
     */
    COMMAND getVolumeDownCommand();
}
