package com.intuso.housemate.client.real.api.internal.factory.hardware;

import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.comms.api.internal.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealHardwareOwner {
    public ChildOverview getAddHardwareCommandDetails();
    public void addHardware(RealHardware hardware);
    public void removeHardware(RealHardware hardware);
}
