package com.intuso.housemate.client.real.api.internal.factory.device;

import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.comms.api.internal.ChildOverview;

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
