package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Task;

public interface RealTask<DRIVER extends TaskDriver>
        extends Task<
                RealCommand,
                RealValue<Boolean>,
                RealValue<String>,
                RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>>,
                RealValue<Boolean>,
                RealList<RealProperty<?>>, RealTask<DRIVER>>,TaskDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isExecuting();

    /**
     * Executes this task
     */
    void executeTask();

    interface Container extends Task.Container<RealList<RealTask<?>>>, RemoveCallback {
        <DRIVER extends TaskDriver> RealTask<DRIVER> createAndAddTask(TaskData data);
        void addTask(RealTask<?> task);
    }

    interface RemoveCallback {
        void removeTask(RealTask<?> task);
    }

    interface Factory {
        RealTask<?> create(TaskData data, RemoveCallback removeCallback);
    }
}
