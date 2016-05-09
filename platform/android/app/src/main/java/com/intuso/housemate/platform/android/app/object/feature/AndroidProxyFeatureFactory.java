package com.intuso.housemate.platform.android.app.object.feature;

import com.intuso.housemate.client.v1_0.proxy.api.object.feature.ProxyFeatureFactory;
import com.intuso.housemate.platform.android.app.object.AndroidProxyFeature;

/**
 * Feature factory for simple proxy features
 */
public class AndroidProxyFeatureFactory
        extends ProxyFeatureFactory<AndroidProxyFeature, AndroidProxyFeatureImpl> {

    @Override
    public AndroidProxyFeatureImpl.PowerControl getPowerControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.PowerControl(feature);
    }

    @Override
    public AndroidProxyFeatureImpl.StatefulPowerControl getStatefulPowerControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.StatefulPowerControl(feature);
    }

    @Override
    public AndroidProxyFeatureImpl.PlaybackControl getPlaybackControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.PlaybackControl(feature);
    }

    @Override
    public AndroidProxyFeatureImpl.StatefulPlaybackControl getStatefulPlaybackControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.StatefulPlaybackControl(feature);
    }

    @Override
    public AndroidProxyFeatureImpl.VolumeControl getVolumeControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.VolumeControl(feature);
    }

    @Override
    public AndroidProxyFeatureImpl.StatefulVolumeControl getStatefulVolumeControl(AndroidProxyFeature feature) {
        return new AndroidProxyFeatureImpl.StatefulVolumeControl(feature);
    }

    @Override
    protected <F extends AndroidProxyFeatureImpl> F getUnknown(AndroidProxyFeature feature) {
        return null;
    }
}
