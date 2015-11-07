package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverBridgeReverse implements TaskDriver {

    private final com.intuso.housemate.client.real.api.internal.driver.TaskDriver taskDriver;

    public TaskDriverBridgeReverse(com.intuso.housemate.client.real.api.internal.driver.TaskDriver taskDriver) {
        this.taskDriver = taskDriver;
    }

    public com.intuso.housemate.client.real.api.internal.driver.TaskDriver getTaskDriver() {
        return taskDriver;
    }

    @Override
    public void execute() {
        taskDriver.execute();
    }
}
