package com.intuso.housemate.object.real.factory.device;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealDevice;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealDeviceOwner {
    public ChildOverview getAddDeviceCommandDetails();
    public void addDevice(RealDevice device);
    public void removeDevice(RealDevice device);
}
