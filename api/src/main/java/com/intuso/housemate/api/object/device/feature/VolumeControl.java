package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;

public interface VolumeControl<COMMAND extends Command<?, ?>>
        extends Feature {

    public final static String ID = "volume-control";

    public final static String VOLUME_UP_COMMAND = "volume-up";
    public final static String VOLUME_DOWN_COMMAND = "volume-down";

    public COMMAND getVolumeUpCommand();
    public COMMAND getVolumeDownCommand();
}
