package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.object.real.annotations.Command;
import com.intuso.housemate.api.object.device.feature.FeatureId;
import com.intuso.housemate.api.HousemateException;

/**
 * Interface to mark real devices that provide volume control
 */
@FeatureId("volume-control")
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
