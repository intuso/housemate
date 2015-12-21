package com.intuso.housemate.client.real.impl.internal.factory.task;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.impl.internal.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 19/03/15.
 */
public class TaskFactoryType extends FactoryType<TaskDriver.Factory<?>> {

    public final static String TYPE_ID = "task-factory";
    public final static String TYPE_NAME = "Task Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new tasks";

    private final static Logger logger = LoggerFactory.getLogger(TaskFactoryType.class);

    @Inject
    protected TaskFactoryType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
