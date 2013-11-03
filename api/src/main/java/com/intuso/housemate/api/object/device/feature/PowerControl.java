package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;

@Id("power-control")
public interface PowerControl<COMMAND extends Command<?, ?>>
        extends Feature {

    public final static String ID = "power-control";

    public final static String ON_COMMAND = "on";
    public final static String OFF_COMMAND = "off";

    public COMMAND getOnCommand();
    public COMMAND getOffCommand();
}
