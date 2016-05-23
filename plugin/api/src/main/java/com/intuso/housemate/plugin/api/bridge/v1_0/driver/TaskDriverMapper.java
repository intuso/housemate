package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverMapper {

    private final Function<com.intuso.housemate.plugin.api.internal.driver.TaskDriver, TaskDriver> toV1_0Function = new Function<com.intuso.housemate.plugin.api.internal.driver.TaskDriver, TaskDriver>() {
        @Override
        public TaskDriver apply(com.intuso.housemate.plugin.api.internal.driver.TaskDriver taskDriver) {
            return map(taskDriver);
        }
    };

    private final Function<TaskDriver, com.intuso.housemate.plugin.api.internal.driver.TaskDriver> fromV1_0Function = new Function<TaskDriver, com.intuso.housemate.plugin.api.internal.driver.TaskDriver>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.driver.TaskDriver apply(TaskDriver taskDriver) {
            return map(taskDriver);
        }
    };

    public Function<com.intuso.housemate.plugin.api.internal.driver.TaskDriver, TaskDriver> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<TaskDriver, com.intuso.housemate.plugin.api.internal.driver.TaskDriver> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM extends com.intuso.housemate.plugin.api.internal.driver.TaskDriver, TO extends TaskDriver>
        TO map(FROM taskDriver) {
        if(taskDriver == null)
            return null;
        else if(taskDriver instanceof TaskDriverBridge)
            return (TO) ((TaskDriverBridge) taskDriver).getTaskDriver();
        return (TO) new TaskDriverBridgeReverse(taskDriver);
    }

    public <FROM extends TaskDriver, TO extends com.intuso.housemate.plugin.api.internal.driver.TaskDriver>
        TO map(FROM taskDriver) {
        if(taskDriver == null)
            return null;
        else if(taskDriver instanceof TaskDriverBridgeReverse)
            return (TO) ((TaskDriverBridgeReverse) taskDriver).getTaskDriver();
        return (TO) new TaskDriverBridge(taskDriver);
    }
}
