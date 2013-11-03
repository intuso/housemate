package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

@Id("stateful-volume-control")
public interface StatefulVolumeControl<COMMAND extends Command<?, ?>, VALUE extends Value<?, ?>>
        extends VolumeControl<COMMAND> {

    public final static String ID = "stateful-volume-control";

    public final static String CURRENT_VOLUME_VALUE = "current-volume";

    public VALUE getCurrentVolumeValue();
}
