package com.intuso.housemate.client.proxy.api.internal.object.feature;

/**
 * Base class for feature factories
 * @param <FEATURE> the feature type
 */
public abstract class ProxyFeatureFactory<FEATURE extends com.intuso.housemate.client.proxy.api.internal.object.ProxyFeature<?, ?, ?>, BASE_RETURN_TYPE> {

    /**
     * Get a feature of a device
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return
     */
    public final <F extends BASE_RETURN_TYPE> F getFeatureAs(FEATURE feature) {
        String id = feature.getId();
        if(id.equals(PowerControl.ID))
            return getPowerControl(feature);
        else if(id.equals(StatefulPowerControl.ID))
            return getStatefulPowerControl(feature);
        else if(id.equals(PlaybackControl.ID))
            return getPlaybackControl(feature);
        else if(id.equals(StatefulPlaybackControl.ID))
            return getStatefulPlaybackControl(feature);
        else if(id.equals(VolumeControl.ID))
            return getVolumeControl(feature);
        else if(id.equals(StatefulVolumeControl.ID))
            return getStatefulVolumeControl(feature);
        return getUnknown(feature);
    }

    /**
     * Get the power control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the power control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getPowerControl(FEATURE feature);

    /**
     * Get the stateful power control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the stateful power control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getStatefulPowerControl(FEATURE feature);

    /**
     * Get the playback control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the playback control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getPlaybackControl(FEATURE feature);

    /**
     * Get the stateful playback control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the stateful playback control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getStatefulPlaybackControl(FEATURE feature);

    /**
     * Get the volume control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the volume control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getVolumeControl(FEATURE feature);

    /**
     * Get the stateful volume control feature
     * @param feature the device to get the feature for
     * @param <F> the feature type
     * @return the stateful volume control feature
     */
    protected abstract <F extends BASE_RETURN_TYPE> F getStatefulVolumeControl(FEATURE feature);

    protected abstract <F extends BASE_RETURN_TYPE> F getUnknown(FEATURE feature);
}
