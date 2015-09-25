package com.intuso.housemate.client.real.api.internal.factory.device;

import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealDeviceFactory<DEVICE extends RealDevice> {
    public DEVICE create(DeviceData data, RealDeviceOwner owner);
}
