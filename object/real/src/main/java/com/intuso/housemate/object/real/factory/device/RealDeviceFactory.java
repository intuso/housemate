package com.intuso.housemate.object.real.factory.device;

import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.RealDevice;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealDeviceFactory<DEVICE extends RealDevice> {
    public DEVICE create(DeviceData data, RealDeviceOwner owner);
}
