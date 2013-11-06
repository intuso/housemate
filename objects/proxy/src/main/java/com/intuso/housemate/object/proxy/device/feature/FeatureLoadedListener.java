package com.intuso.housemate.object.proxy.device.feature;

import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.feature.Feature;

/**
 * Listener interface for when a feature has finished loading
 * @param <DEVICE> the device type
 * @param <FEATURE> the feature type
 */
public interface FeatureLoadedListener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, FEATURE extends Feature> {

    /**
     * Callback method for when the feature has finished loading
     * @param device the device the feature was loaded for
     * @param feature the feature that was loaded
     */
    public void featureLoaded(DEVICE device, FEATURE feature);
}
