package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.AutomationData;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.object.api.internal.Condition;

public interface RealAutomation
        extends Automation<RealCommand,
                RealCommand,
                RealCommand,
                RealCommand,
                RealValue<Boolean>,
                RealValue<String>,
                RealCondition<?>,
                RealList<? extends RealCondition<?>>,
                RealTask<?>,
                RealList<? extends RealTask<?>>,
                RealAutomation>,
        Condition.Listener<RealCondition<?>>,
        RealCondition.RemovedListener {

    boolean isRunning();

    interface RemovedListener {
        void automationRemoved(RealAutomation automation);
    }

    interface Factory {
        RealAutomation create(AutomationData data, RemovedListener removedListener);
    }
}
