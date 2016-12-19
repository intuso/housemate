package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverFactoryBridgeReverse implements TaskDriver.Factory<TaskDriver> {

    private final com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<?> factory;
    private final TaskDriverMapper taskDriverMapper;

    @Inject
    public TaskDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<?> factory,
                                          TaskDriverMapper taskDriverMapper) {
        this.factory = factory;
        this.taskDriverMapper = taskDriverMapper;
    }

    public com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public TaskDriver create(Logger logger, TaskDriver.Callback callback) {
        return taskDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.api.internal.driver.TaskDriver.Callback {

        private final TaskDriver.Callback callback;

        private CallbackBridge(TaskDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        TaskDriverFactoryBridgeReverse create(com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<?> taskDriverFactory);
    }
}
