package com.intuso.housemate.object.proxy.device.feature;

import com.intuso.housemate.api.object.device.feature.*;
import com.intuso.housemate.object.proxy.ProxyDevice;

/**
 * Base class for feature factories
 * @param <FEATURE> the feature type
 * @param <DEVICE> the device type
 */
public abstract class ProxyFeatureFactory<
        FEATURE extends ProxyFeature<?, ?>,
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

    /**
     * Get a feature of a device
     * @param id the id of the feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return
     */
    public final <F extends FEATURE> F getFeature(String id, DEVICE device) {
        if(id.equals(PowerControl.ID))
            return getPowerControl(device);
        else if(id.equals(StatefulPowerControl.ID))
            return getStatefulPowerControl(device);
        else if(id.equals(PlaybackControl.ID))
            return getPlaybackControl(device);
        else if(id.equals(StatefulPlaybackControl.ID))
            return getStatefulPlaybackControl(device);
        else if(id.equals(VolumeControl.ID))
            return getVolumeControl(device);
        else if(id.equals(StatefulVolumeControl.ID))
            return getStatefulVolumeControl(device);
        return null;
    }

    /**
     * Get the power control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the power control feature
     */
    protected abstract <F extends FEATURE> F getPowerControl(DEVICE device);

    /**
     * Get the stateful power control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the stateful power control feature
     */
    protected abstract <F extends FEATURE> F getStatefulPowerControl(DEVICE device);

    /**
     * Get the playback control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the playback control feature
     */
    protected abstract <F extends FEATURE> F getPlaybackControl(DEVICE device);

    /**
     * Get the stateful playback control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the stateful playback control feature
     */
    protected abstract <F extends FEATURE> F getStatefulPlaybackControl(DEVICE device);

    /**
     * Get the volume control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the volume control feature
     */
    protected abstract <F extends FEATURE> F getVolumeControl(DEVICE device);

    /**
     * Get the stateful volume control feature
     * @param device the device to get the feature for
     * @param <F> the feature type
     * @return the stateful volume control feature
     */
    protected abstract <F extends FEATURE> F getStatefulVolumeControl(DEVICE device);
}
