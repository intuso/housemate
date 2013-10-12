package com.intuso.housemate.broker.factory;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.*;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Arrays;
import java.util.HashMap;
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

    private final BrokerGeneralResources resources;
    private final Map<String, BrokerTaskFactory<?>> factories;
    private final TaskFactoryType type;

    public TaskFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, BrokerTaskFactory<?>>();
        type = new TaskFactoryType(resources.getClientResources());
        resources.addPluginListener(this, true);
    }

    public TaskFactoryType getType() {
        return type;
    }

    public BrokerRealCommand createAddTaskCommand(String commandId, String commandName, String commandDescription,
                                                  final BrokerRealTaskOwner owner,
                                                  final BrokerRealList<TaskData, BrokerRealTask> list) {
        return new BrokerRealCommand(resources.getRealResources(), commandId, commandName, commandDescription, Arrays.asList(
                new BrokerRealParameter<String>(resources.getRealResources(), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealParameter<String>(resources.getRealResources(), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealParameter<BrokerTaskFactory<?>>(resources.getRealResources(), TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                BrokerRealTask task = createTask(values, owner);
                // todo process annotations
                list.add(task);
                resources.getStorage().saveValues(list.getPath(), task.getId(), values);
            }
        };
    }

    public BrokerRealTask createTask(TypeInstanceMap values, BrokerRealTaskOwner owner) throws HousemateException {
        TypeInstances taskType = values.get(TYPE_PARAMETER_ID);
        if(taskType == null || taskType.getFirstValue() == null)
            throw new HousemateException("No task type specified");
        TypeInstances name = values.get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No task name specified");
        TypeInstances description = values.get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No task description specified");
        BrokerTaskFactory<?> taskFactory = type.deserialise(taskType.get(0));
        if(taskFactory == null)
            throw new HousemateException("No factory known for task type " + taskType);
        return taskFactory.create(resources.getRealResources(), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), owner);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(BrokerTaskFactory<?> factory : plugin.getTaskFactories()) {
            resources.getLog().d("Adding new task factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(resources.getClientResources(), factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(BrokerTaskFactory<?> factory : plugin.getTaskFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class TaskFactoryType extends RealChoiceType<BrokerTaskFactory<?>> {
        protected TaskFactoryType(RealResources resources) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(BrokerTaskFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public BrokerTaskFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}