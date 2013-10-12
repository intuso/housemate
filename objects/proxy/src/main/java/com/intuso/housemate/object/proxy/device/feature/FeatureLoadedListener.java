package com.intuso.housemate.object.proxy.device.feature;

import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.feature.Feature;

public interface FeatureLoadedListener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, FEATURE extends Feature> {
    public void featureLoaded(DEVICE device, FEATURE feature);
}
