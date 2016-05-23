package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.intuso.housemate.plugin.v1_0.api.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverBridgeReverse implements TaskDriver {

    private final com.intuso.housemate.plugin.api.internal.driver.TaskDriver taskDriver;

    public TaskDriverBridgeReverse(com.intuso.housemate.plugin.api.internal.driver.TaskDriver taskDriver) {
        this.taskDriver = taskDriver;
    }

    public com.intuso.housemate.plugin.api.internal.driver.TaskDriver getTaskDriver() {
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
