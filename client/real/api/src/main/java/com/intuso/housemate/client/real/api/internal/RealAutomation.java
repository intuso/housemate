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
        BOOLEAN_VALUE, COMMAND,
        STRING_VALUE, COMMAND,
        CONDITIONS,
        TASKS,
        AUTOMATION> {

    boolean isRunning();
}
