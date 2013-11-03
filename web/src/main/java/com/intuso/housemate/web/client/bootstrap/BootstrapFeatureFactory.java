package com.intuso.housemate.web.client.bootstrap;

import com.intuso.housemate.web.client.bootstrap.widget.device.feature.*;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;

public class BootstrapFeatureFactory extends GWTProxyFeatureFactory {
    @Override
    public PowerControl getOnOff(GWTProxyDevice device) {
        return new PowerControl(device);
    }

    @Override
    public PlaybackControl getPlaybackControl(GWTProxyDevice device) {
        return new PlaybackControl(device);
    }

    @Override
    public StatefulPlaybackControl getStatefulPlaybackControl(GWTProxyDevice device) {
        return new StatefulPlaybackControl(device);
    }

    @Override
    public VolumeControl getVolumeControl(GWTProxyDevice device) {
        return new VolumeControl(device);
    }

    @Override
    public StatefulVolumeControl getStatefulVolumeControl(GWTProxyDevice device) {
        return new StatefulVolumeControl(device);
    }
}
