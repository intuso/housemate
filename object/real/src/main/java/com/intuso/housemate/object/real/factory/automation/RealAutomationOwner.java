package com.intuso.housemate.object.real.factory.automation;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealAutomation;

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
