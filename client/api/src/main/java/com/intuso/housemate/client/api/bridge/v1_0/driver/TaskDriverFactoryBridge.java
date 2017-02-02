package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverFactoryBridge implements com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<com.intuso.housemate.client.api.internal.driver.TaskDriver> {

    private final TaskDriver.Factory<?> factory;
    private final TaskDriverMapper taskDriverMapper;

    @Inject
    public TaskDriverFactoryBridge(@Assisted TaskDriver.Factory<?> factory,
                                   TaskDriverMapper taskDriverMapper) {
        this.factory = factory;
        this.taskDriverMapper = taskDriverMapper;
    }

    public TaskDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public com.intuso.housemate.client.api.internal.driver.TaskDriver create(Logger logger, com.intuso.housemate.client.api.internal.driver.TaskDriver.Callback callback) {
        return taskDriverMapper.map(factory.create(logger, new TaskDriverBridge.CallbackBridge(callback)));
    }

    public interface Factory {
        TaskDriverFactoryBridge create(TaskDriver.Factory<?> taskDriverFactory);
    }
}
