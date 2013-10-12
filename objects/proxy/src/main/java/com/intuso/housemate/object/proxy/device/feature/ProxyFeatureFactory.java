package com.intuso.housemate.object.proxy.device.feature;

import com.intuso.housemate.api.object.device.feature.OnOff;
import com.intuso.housemate.api.object.device.feature.PlaybackControl;
import com.intuso.housemate.api.object.device.feature.VolumeControl;
import com.intuso.housemate.object.proxy.ProxyDevice;

public abstract class ProxyFeatureFactory<
        FEATURE extends ProxyFeature<?, ?>,
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

    public final <F extends FEATURE> F getFeature(String id, DEVICE device) {
        if(id.equals(OnOff.ID))
            return getOnOff(device);
        else if(id.equals(PlaybackControl.ID))
            return getPlaybackControl(device);
        else if(id.equals(VolumeControl.ID))
            return getVolumeControl(device);
        return null;
    }

    public abstract <F extends FEATURE> F getOnOff(DEVICE device);
    public abstract <F extends FEATURE> F getPlaybackControl(DEVICE device);
    public abstract <F extends FEATURE> F getVolumeControl(DEVICE device);
}
