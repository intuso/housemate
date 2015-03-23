package com.intuso.housemate.object.real.factory.hardware;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealHardware;

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
