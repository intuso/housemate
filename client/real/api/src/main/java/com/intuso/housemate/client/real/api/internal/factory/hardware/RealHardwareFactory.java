package com.intuso.housemate.client.real.api.internal.factory.hardware;

import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealHardwareFactory<HARDWARE extends RealHardware> {
    public HARDWARE create(HardwareData data, RealHardwareOwner owner);
}
