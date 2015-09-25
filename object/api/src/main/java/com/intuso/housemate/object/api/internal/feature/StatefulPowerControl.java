package com.intuso.housemate.object.api.internal.feature;

import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Value;

/**
 * Interface for all devices that allow power control with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
public interface StatefulPowerControl<COMMAND extends Command<?, ?, ?, ?>, VALUE extends Value<?, ?>>
        extends PowerControl<COMMAND> {

    String ID = "stateful-power-control";

    /**
     * Get the value that describes if the device is currently on
     * @return the value that describes if the device is currently on
     */
    VALUE getIsOnValue();

    /**
     * is the device currently on
     * @return true if the device is currently on
     */
    boolean isOn();
}
