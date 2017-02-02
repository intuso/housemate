package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverBridgeReverse implements TaskDriver {

    private final com.intuso.housemate.client.api.internal.driver.TaskDriver taskDriver;

    public TaskDriverBridgeReverse(com.intuso.housemate.client.api.internal.driver.TaskDriver taskDriver) {
        this.taskDriver = taskDriver;
    }

    public com.intuso.housemate.client.api.internal.driver.TaskDriver getTaskDriver() {
        return taskDriver;
    }

    @Override
    public void init(Logger logger, Callback callback) {
        taskDriver.init(logger, new CallbackBridge(callback));
    }

    @Override
    public void uninit() {
        taskDriver.uninit();
    }

    @Override
    public void execute() {
        taskDriver.execute();
    }

    public static class CallbackBridge implements com.intuso.housemate.client.api.internal.driver.TaskDriver.Callback {

        private final Callback callback;

        public CallbackBridge(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }
}
