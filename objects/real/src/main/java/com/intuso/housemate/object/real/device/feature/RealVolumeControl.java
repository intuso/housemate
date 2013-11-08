package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;

/**
 * Interface to mark real devices that provide volume control
 */
@Id("volume-control")
public interface RealVolumeControl extends RealFeature {

    /**
     * Callback for when the volume should be muted
     * @throws HousemateException
     */
    @Command(id = "mute", name = "Mute", description = "Mute")
    public void mute() throws HousemateException;

    /**
     * Callback for when the volume should be increased
     * @throws HousemateException
     */
    @Command(id = "volume-up", name = "Volume Up", description = "Volume up")
    public void volumeUp() throws HousemateException;

    /**
     * Callback for when the volume should be decreased
     * @throws HousemateException
     */
    @Command(id = "volume-down", name = "Volume Down", description = "Volume down")
    public void volumeDown() throws HousemateException;
}
