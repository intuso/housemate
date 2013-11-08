package com.intuso.housemate.api.object.device.feature;

import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.command.Command;

/**
 * Interface for all devices that allow volume control
 * @param <COMMAND> the command type
 */
@Id("volume-control")
public interface VolumeControl<COMMAND extends Command<?, ?>>
        extends Feature {

    public final static String ID = "volume-control";

    public final static String MUTE_COMMAND = "mute";
    public final static String VOLUME_UP_COMMAND = "volume-up";
    public final static String VOLUME_DOWN_COMMAND = "volume-down";

    /**
     * Get the command to mute the volume
     * @return the command to mute  the volume
     */
    public COMMAND getMuteCommand();

    /**
     * Get the command to increase the volume
     * @return the command to increase the volume
     */
    public COMMAND getVolumeUpCommand();

    /**
     * Get the command to decrease the volume
     * @return the command to decrease the volume
     */
    public COMMAND getVolumeDownCommand();
}
