package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;

/**
 * Interface for all devices that allow power control
 * @param <COMMAND> the command type
 */
@Id("power-control")
public interface PowerControl<COMMAND extends Command<?, ?>>
        extends Feature {

    public final static String ID = "power-control";

    public final static String ON_COMMAND = "on";
    public final static String OFF_COMMAND = "off";

    /**
     * Get the command to power the device on
     * @return the command to power the device on
     */
    public COMMAND getOnCommand();

    /**
     * Get the command to power the device off
     * @return the command to power the device off
     */
    public COMMAND getOffCommand();
}
