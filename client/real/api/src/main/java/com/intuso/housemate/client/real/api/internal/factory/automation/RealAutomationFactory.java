package com.intuso.housemate.client.real.api.internal.factory.automation;

import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.comms.api.internal.payload.AutomationData;

/**
* Created by tomc on 20/03/15.
*/
public interface RealAutomationFactory {
    public RealAutomation create(AutomationData data, RealAutomationOwner owner);
}
