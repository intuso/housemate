package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;

/**
 * Feature factory for simple proxy features
 */
public class SimpleProxyFeatureFactory
        extends ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyObject.Device> {

    @Override
    public SimpleProxyFeature.PowerControl getPowerControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.PowerControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulPowerControl getStatefulPowerControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.StatefulPowerControl(device);
    }

    @Override
    public SimpleProxyFeature.PlaybackControl getPlaybackControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.PlaybackControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulPlaybackControl getStatefulPlaybackControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.StatefulPlaybackControl(device);
    }

    @Override
    public SimpleProxyFeature.VolumeControl getVolumeControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.VolumeControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulVolumeControl getStatefulVolumeControl(SimpleProxyObject.Device device) {
        return new SimpleProxyFeature.StatefulVolumeControl(device);
    }
}
