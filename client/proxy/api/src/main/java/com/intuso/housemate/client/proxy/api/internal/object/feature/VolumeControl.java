package com.intuso.housemate.client.proxy.api.internal.object.feature;

import com.intuso.housemate.client.api.internal.object.Command;

/**
 * Interface for all devices that allow volume control
 * @param <COMMAND> the command type
 */
public interface VolumeControl<COMMAND extends Command<?, ?, ?, ?>> extends ProxyFeature {

    String ID = "volume";

    /**
     * Get the command to mute the volume
     * @return the command to mute  the volume
     */
    COMMAND getMuteCommand();

    /**
     * Get the command to increase the volume
     * @return the command to increase the volume
     */
    COMMAND getVolumeUpCommand();

    /**
     * Get the command to decrease the volume
     * @return the command to decrease the volume
     */
    COMMAND getVolumeDownCommand();
}
