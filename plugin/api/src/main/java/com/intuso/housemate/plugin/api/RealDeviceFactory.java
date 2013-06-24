package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

/**
 */
public interface RealDeviceFactory<D extends RealDevice> {
    public String getTypeId();
    public String getTypeName();
    public String getTypeDescription();
    public D create(RealResources resources, String id, String name, String description) throws HousemateException;
}
