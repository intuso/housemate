package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Automation;

public interface RealAutomation<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        CONDITIONS extends RealList<? extends RealCondition<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        TASKS extends RealList<? extends RealTask<?, ?, ?, ?, ?, ?>, ?>,
        AUTOMATION extends RealAutomation<COMMAND, BOOLEAN_VALUE, STRING_VALUE, CONDITIONS, TASKS, AUTOMATION>>
        extends Automation<COMMAND,
        COMMAND,
        COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        CONDITIONS,
        TASKS,
        AUTOMATION> {

    boolean isRunning();

    interface Container<AUTOMATION extends RealAutomation<?, ?, ?, ?, ?, ?>, AUTOMATIONS extends RealList<? extends AUTOMATION, ?>> extends Automation.Container<AUTOMATIONS>, RemoveCallback<AUTOMATION> {
        void addAutomation(AUTOMATION automation);
    }

    interface RemoveCallback<AUTOMATION extends RealAutomation<?, ?, ?, ?, ?, ?>> {
        void removeAutomation(AUTOMATION automation);
    }
}
