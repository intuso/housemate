package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * Interface for all devices that allow power control with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
@Id("stateful-power-control")
public interface StatefulPowerControl<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends PowerControl<COMMAND> {

    public final static String ID = "stateful-power-control";

    public final static String IS_ON_VALUE = "is-on";

    /**
     * Get the value that describes if the device is currently on
     * @return the value that describes if the device is currently on
     */
    public VALUE getIsOnValue();

    /**
     * is the device currently on
     * @return true if the device is currently on
     */
    public boolean isOn();
}
