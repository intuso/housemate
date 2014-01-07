package com.intuso.housemate.server.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;

/**
 */
public final class TaskFactory implements PluginListener {

    public final static String TYPE_ID = "task-factory";
    public final static String TYPE_NAME = "Task Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new tasks";

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new task";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "Description for the new task";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new task";

    private final Log log;
    private final Storage storage;

    private final Map<String, ServerTaskFactory<?>> factories = Maps.newHashMap();
    private final TaskFactoryType type;

    @Inject
    public TaskFactory(Log log, Storage storage, PluginManager pluginManager) {
        this.log = log;
        this.storage = storage;
        type = new TaskFactoryType(log);
        pluginManager.addPluginListener(this, true);
    }

    public TaskFactoryType getType() {
        return type;
    }

    public ServerRealCommand createAddTaskCommand(String commandId, String commandName, String commandDescription,
                                                  final ServerRealTaskOwner owner,
                                                  final ServerRealList<TaskData, ServerRealTask> list) {
        return new ServerRealCommand(log, commandId, commandName, commandDescription, Arrays.asList(
                new ServerRealParameter<String>(log, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(log)),
                new ServerRealParameter<String>(log, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(log)),
                new ServerRealParameter<ServerTaskFactory<?>>(log, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                ServerRealTask task = createTask(values, owner);
                // todo process annotations
                list.add(task);
                storage.saveValues(list.getPath(), task.getId(), values);
            }
        };
    }

    public ServerRealTask createTask(TypeInstanceMap values, ServerRealTaskOwner owner) throws HousemateException {
        TypeInstances taskType = values.get(TYPE_PARAMETER_ID);
        if(taskType == null || taskType.getFirstValue() == null)
            throw new HousemateException("No task type specified");
        TypeInstances name = values.get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No task name specified");
        TypeInstances description = values.get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No task description specified");
        ServerTaskFactory<?> taskFactory = type.deserialise(taskType.get(0));
        if(taskFactory == null)
            throw new HousemateException("No factory known for task type " + taskType);
        return taskFactory.create(log, name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), owner);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(ServerTaskFactory<?> factory : plugin.getTaskFactories()) {
            log.d("Adding new task factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(log, factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(ServerTaskFactory<?> factory : plugin.getTaskFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class TaskFactoryType extends RealChoiceType<ServerTaskFactory<?>> {
        protected TaskFactoryType(Log log) {
            super(log, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(ServerTaskFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public ServerTaskFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}