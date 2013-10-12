package com.intuso.housemate.object.proxy.device.feature;

import com.intuso.housemate.api.object.device.feature.Feature;
import com.intuso.housemate.object.proxy.ProxyDevice;

import java.util.Set;

public interface ProxyFeature<
            FEATURE extends ProxyFeature<?, ?>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends Feature {

    public Set<String> getCommandIds();
    public Set<String> getValueIds();
    public Set<String> getPropertyIds();
    public void load(FeatureLoadedListener<DEVICE, FEATURE> listener);
    public FEATURE getThis();
}
