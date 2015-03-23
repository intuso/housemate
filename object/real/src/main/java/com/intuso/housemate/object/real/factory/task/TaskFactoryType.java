package com.intuso.housemate.object.real.factory.task;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class TaskFactoryType extends FactoryType<RealTaskFactory<?>> {

    public final static String TYPE_ID = "task-factory";
    public final static String TYPE_NAME = "Task Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new tasks";

    @Inject
    protected TaskFactoryType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
