package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.api.internal.object.Task;

public interface RealTask<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginDependency<TaskDriver.Factory<?>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        TASK extends RealTask<COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, TASK>>
        extends Task<COMMAND,
        COMMAND,
        STRING_VALUE, DRIVER_PROPERTY, BOOLEAN_VALUE, BOOLEAN_VALUE,
        PROPERTIES,
        TASK>,
        TaskDriver.Callback {

    <DRIVER extends TaskDriver> DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isExecuting();

    /**
     * Executes this task
     */
    void executeTask();
}
