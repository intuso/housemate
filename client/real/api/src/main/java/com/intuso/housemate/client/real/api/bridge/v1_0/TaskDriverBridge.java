package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverBridge implements TaskDriver {

    private final com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver taskDriver;

    public TaskDriverBridge(com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver taskDriver) {
        this.taskDriver = taskDriver;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver getTaskDriver() {
        return taskDriver;
    }

    @Override
    public void start() {
        taskDriver.start();
    }

    @Override
    public void stop() {
        taskDriver.stop();
    }

    @Override
    public void execute() {
        taskDriver.execute();
    }
}
