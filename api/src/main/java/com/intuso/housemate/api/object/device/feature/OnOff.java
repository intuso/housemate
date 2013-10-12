package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

public interface OnOff<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends Feature {

    public final static String ID = "on-off";

    public final static String ON_COMMAND = "on";
    public final static String OFF_COMMAND = "off";
    public final static String IS_ON_VALUE = "is-on";

    public COMMAND getOnCommand();
    public COMMAND getOffCommand();
    public VALUE getIsOnValue();
    public boolean isOn();
}
