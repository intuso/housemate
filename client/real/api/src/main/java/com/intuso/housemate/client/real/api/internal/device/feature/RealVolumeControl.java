package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Command;
import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;

/**
 * Interface to mark real devices that provide volume control
 */
@FeatureId("volume")
public interface RealVolumeControl extends RealFeature {

    /**
     * Callback for when the volume should be muted
     */
    @Command(id = "mute", name = "Mute", description = "Mute")
    public void mute();

    /**
     * Callback for when the volume should be increased
     */
    @Command(id = "volume-up", name = "Volume Up", description = "Volume up")
    public void volumeUp();

    /**
     * Callback for when the volume should be decreased
     */
    @Command(id = "volume-down", name = "Volume Down", description = "Volume down")
    public void volumeDown();
}
