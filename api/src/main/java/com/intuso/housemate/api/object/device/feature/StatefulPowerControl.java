package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

@Id("stateful-power-control")
public interface StatefulPowerControl<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends PowerControl<COMMAND> {

    public final static String ID = "stateful-power-control";

    public final static String IS_ON_VALUE = "is-on";

    public VALUE getIsOnValue();
    public boolean isOn();
}
