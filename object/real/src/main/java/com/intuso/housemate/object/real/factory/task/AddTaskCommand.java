package com.intuso.housemate.object.real.factory.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddTaskCommand extends RealCommand {

    private final TaskFactoryType taskFactoryType;
    private final AnnotationProcessor annotationProcessor;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealTaskOwner owner;

    @Inject
    protected AddTaskCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                             TaskFactoryType taskFactoryType, AnnotationProcessor annotationProcessor,
                             RealList<TypeData<?>, RealType<?, ?, ?>> types,
                             @Assisted RealTaskOwner owner) {
        super(log, listenersFactory,
                owner.getAddTaskCommandDetails().getId(), owner.getAddTaskCommandDetails().getName(), owner.getAddTaskCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, RealRoot.NAME_PARAMETER_ID, RealRoot.NAME_PARAMETER_NAME, RealRoot.NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.DESCRIPTION_PARAMETER_ID, RealRoot.DESCRIPTION_PARAMETER_NAME, RealRoot.DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.TYPE_PARAMETER_ID, RealRoot.TYPE_PARAMETER_NAME, RealRoot.TYPE_PARAMETER_DESCRIPTION, taskFactoryType));
        this.taskFactoryType = taskFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) throws HousemateException {
        TypeInstances taskType = values.getChildren().get(RealRoot.TYPE_PARAMETER_ID);
        if(taskType == null || taskType.getFirstValue() == null)
            throw new HousemateException("No type specified");
        RealTaskFactory<?> taskFactory = taskFactoryType.deserialise(taskType.getElements().get(0));
        if(taskFactory == null)
            throw new HousemateException("No factory known for task type " + taskType);
        TypeInstances name = values.getChildren().get(RealRoot.NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        TypeInstances description = values.getChildren().get(RealRoot.DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        RealTask task = taskFactory.create(new TaskData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, task);
        owner.addTask(task);
    }

    public interface Factory {
        public AddTaskCommand create(RealTaskOwner owner);
    }
}
