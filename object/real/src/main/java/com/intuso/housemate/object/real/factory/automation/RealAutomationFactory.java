package com.intuso.housemate.object.real.factory.automation;

import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.object.real.RealAutomation;

/**
* Created by tomc on 20/03/15.
*/
public interface RealAutomationFactory {
    public RealAutomation create(AutomationData data, RealAutomationOwner owner);
}
