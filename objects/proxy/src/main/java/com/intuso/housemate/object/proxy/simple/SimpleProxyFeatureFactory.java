package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;

public class SimpleProxyFeatureFactory
        extends ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyObject.Device> {

    @Override
    public SimpleProxyFeature.OnOff getOnOff(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.OnOff(device);
    }

    @Override
    public SimpleProxyFeature.PlaybackControl getPlaybackControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.PlaybackControl(device);
    }

    @Override
    public SimpleProxyFeature.VolumeControl getVolumeControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.VolumeControl(device);
    }
}
