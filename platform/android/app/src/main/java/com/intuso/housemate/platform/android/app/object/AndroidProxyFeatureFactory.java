package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeatureFactory;

/**
 * Feature factory for simple proxy features
 */
public class AndroidProxyFeatureFactory
        extends ProxyFeatureFactory<AndroidProxyFeature, AndroidProxyDevice> {

    @Override
    public AndroidProxyFeature.PowerControl getPowerControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.PowerControl(device);
    }

    @Override
    public AndroidProxyFeature.StatefulPowerControl getStatefulPowerControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.StatefulPowerControl(device);
    }

    @Override
    public AndroidProxyFeature.PlaybackControl getPlaybackControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.PlaybackControl(device);
    }

    @Override
    public AndroidProxyFeature.StatefulPlaybackControl getStatefulPlaybackControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.StatefulPlaybackControl(device);
    }

    @Override
    public AndroidProxyFeature.VolumeControl getVolumeControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.VolumeControl(device);
    }

    @Override
    public AndroidProxyFeature.StatefulVolumeControl getStatefulVolumeControl(AndroidProxyDevice device) {
        return new AndroidProxyFeature.StatefulVolumeControl(device);
    }
}
