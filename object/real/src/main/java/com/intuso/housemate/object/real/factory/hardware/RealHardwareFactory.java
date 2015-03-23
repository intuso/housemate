package com.intuso.housemate.object.real.factory.hardware;

import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.object.real.RealHardware;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealHardwareFactory<HARDWARE extends RealHardware> {
    public HARDWARE create(HardwareData data, RealHardwareOwner owner);
}
