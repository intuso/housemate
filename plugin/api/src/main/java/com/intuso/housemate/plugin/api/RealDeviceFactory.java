package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public interface RealDeviceFactory<D extends RealDevice> {
    public String getTypeId();
    public String getTypeName();
    public String getTypeDescription();
    public D create(RealResources resources, String id, String name, String description) throws HousemateException;
}
