package com.intuso.housemate.client.real.api.internal.factory.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddTaskCommand extends RealCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new task";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new task";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new task";

    private final TaskFactoryType taskFactoryType;
    private final Callback callback;
    private final RealTask.Factory taskFactory;
    private final RealTask.RemovedListener removedListener;

    @Inject
    protected AddTaskCommand(Log log,
                             ListenersFactory listenersFactory,
                             StringType stringType,
                             TaskFactoryType taskFactoryType,
                             RealTask.Factory taskFactory,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted Callback callback,
                             @Assisted RealTask.RemovedListener removedListener) {
        super(log, listenersFactory, id, name, description,
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, taskFactoryType));
        this.taskFactoryType = taskFactoryType;
        this.callback = callback;
        this.taskFactory = taskFactory;
        this.removedListener = removedListener;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealTask task = taskFactory.create(new TaskData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removedListener);
        callback.addTask(task);
        TypeInstances taskType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(taskType != null && taskType.getFirstValue() != null) {
            TaskDriver.Factory<?> driverFactory = taskFactoryType.deserialise(taskType.getElements().get(0));
            task.getDriverProperty().setTypedValues(driverFactory);
        }
    }

    public interface Callback {
        void addTask(RealTask task);
    }

    public interface Factory {
        AddTaskCommand create(@Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              Callback callback,
                              RealTask.RemovedListener removedListener);
    }
}
