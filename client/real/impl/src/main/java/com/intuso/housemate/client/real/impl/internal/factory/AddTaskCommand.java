package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealTaskImpl;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddTaskCommand {
    
    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new task";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new task";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new task";

    public interface Callback {
        void addTask(RealTaskImpl task);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final TaskFactoryType taskFactoryType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealTaskImpl.Factory taskFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, TaskFactoryType taskFactoryType, RealCommandImpl.Factory commandFactory, RealTaskImpl.Factory TaskFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.taskFactoryType = taskFactoryType;
            this.commandFactory = commandFactory;
            this.taskFactory = TaskFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealTask.RemoveCallback<RealTaskImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, taskFactoryType, taskFactory, callback, removeCallback),
                    Lists.newArrayList(new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, TYPE_PARAMETER_ID),
                                    TYPE_PARAMETER_ID,
                                    TYPE_PARAMETER_NAME,
                                    TYPE_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    taskFactoryType)));
        }
    }

    private static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final TaskFactoryType taskFactoryType;
        private final RealTaskImpl.Factory taskFactory;
        private final Callback callback;
        private final RealTask.RemoveCallback<RealTaskImpl> removeCallback;

        private Performer(Logger logger, TaskFactoryType taskFactoryType, RealTaskImpl.Factory taskFactory, Callback callback, RealTask.RemoveCallback<RealTaskImpl> removeCallback) {
            this.logger = logger;
            this.taskFactoryType = taskFactoryType;
            this.callback = callback;
            this.taskFactory = taskFactory;
            this.removeCallback = removeCallback;
        }

        @Override
        public void perform(Type.InstanceMap values) {
            Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
            if(name == null || name.getFirstValue() == null)
                throw new HousemateException("No name specified");
            Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
            if(description == null || description.getFirstValue() == null)
                throw new HousemateException("No description specified");
            RealTaskImpl task = taskFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addTask(task);
            Type.Instances taskType = values.getChildren().get(TYPE_PARAMETER_ID);
            if(taskType != null && taskType.getFirstValue() != null)
                ((RealProperty)task.getDriverProperty()).setValue(taskFactoryType.deserialise(taskType.getElements().get(0)));
        }
    }
}
