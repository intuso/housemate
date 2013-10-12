package com.intuso.housemate.web.client.bootstrap;

import com.intuso.housemate.web.client.bootstrap.widget.device.feature.OnOff;
import com.intuso.housemate.web.client.bootstrap.widget.device.feature.PlaybackControl;
import com.intuso.housemate.web.client.bootstrap.widget.device.feature.VolumeControl;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;

public class BootstrapFeatureFactory extends GWTProxyFeatureFactory {
    @Override
    public OnOff getOnOff(GWTProxyDevice device) {
        return new OnOff(device);
    }

    @Override
    public PlaybackControl getPlaybackControl(GWTProxyDevice device) {
        return new PlaybackControl(device);
    }

    @Override
    public VolumeControl getVolumeControl(GWTProxyDevice device) {
        return new VolumeControl(device);
    }
}
