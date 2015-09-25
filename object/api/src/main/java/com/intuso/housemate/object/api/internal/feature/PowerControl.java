package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Device;

/**
 * Interface for all devices that allow power control
 * @param <COMMAND> the command type
 */
public interface PowerControl<COMMAND extends Command<?, ?, ?, ?>>
        extends Device.Feature {

    String ID = "power-control";

    /**
     * Get the command to power the device on
     * @return the command to power the device on
     */
    COMMAND getOnCommand();

    /**
     * Get the command to power the device off
     * @return the command to power the device off
     */
    COMMAND getOffCommand();
}
