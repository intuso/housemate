package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;

@Id("volume-control")
public interface RealVolumeControl extends RealFeature {
    @Command(id = "volume-up", name = "Volume Up", description = "Volume up")
    public void volumeUp() throws HousemateException;
    @Command(id = "volume-down", name = "Volume Down", description = "Volume down")
    public void volumeDown() throws HousemateException;
}
