package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import org.slf4j.Logger;

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

    public static class CallbackBridge implements TaskDriver.Callback {

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
