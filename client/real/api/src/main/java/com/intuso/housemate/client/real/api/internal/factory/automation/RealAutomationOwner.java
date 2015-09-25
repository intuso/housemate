package com.intuso.housemate.client.real.api.internal.factory.automation;

import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.comms.api.internal.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealAutomationOwner {
    public ChildOverview getAddAutomationCommandDetails();
    public void addAutomation(RealAutomation automation);
    public void removeAutomation(RealAutomation automation);
}
