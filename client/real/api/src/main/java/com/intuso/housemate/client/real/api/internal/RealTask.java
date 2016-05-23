package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Task;
import com.intuso.housemate.plugin.api.internal.driver.PluginResource;
import com.intuso.housemate.plugin.api.internal.driver.TaskDriver;

public interface RealTask<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginResource<TaskDriver.Factory<?>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        TASK extends RealTask<COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, TASK>>
        extends Task<COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        DRIVER_PROPERTY,
        BOOLEAN_VALUE,
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

    interface Container<TASK extends RealTask<?, ?, ?, ?, ?, ?>, TASKS extends RealList<? extends TASK, ?>> extends Task.Container<TASKS>, RemoveCallback<TASK> {
        void addTask(TASK task);
    }

    interface RemoveCallback<TASK extends RealTask<?, ?, ?, ?, ?, ?>> {
        void removeTask(TASK task);
    }
}
