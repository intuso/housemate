package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverBridge implements com.intuso.housemate.client.api.internal.driver.TaskDriver {

    private final TaskDriver taskDriver;

    public TaskDriverBridge(TaskDriver taskDriver) {
        this.taskDriver = taskDriver;
    }

    public TaskDriver getTaskDriver() {
        return taskDriver;
    }

    @Override
    public void startTask() {
        taskDriver.startTask();
    }

    @Override
    public void stopTask() {
        taskDriver.stopTask();
    }

    @Override
    public void execute() {
        taskDriver.execute();
    }
}
