package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.object.device.feature.StatefulVolumeControl;

@Id("stateful-volume-control")
public interface RealStatefulVolumeControl extends RealVolumeControl {
    public interface Values {
        @Value(id = StatefulVolumeControl.CURRENT_VOLUME_VALUE, name = "Current Volume", description = "THe device's current volume", typeId = "integer")
        void currentVolume(int currentVolume);
    }
}
