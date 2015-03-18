package com.intuso.housemate.server.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.host.PluginListener;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.housemate.realclient.factory.Factory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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
    private final ListenersFactory listenersFactory;
    private final Persistence persistence;

    private final Map<String, Factory.Entry<ServerTaskFactory<?>>> factoryEntries = Maps.newHashMap();
    private final TaskFactoryType type;

    @Inject
    public TaskFactory(Log log, ListenersFactory listenersFactory, Persistence persistence, PluginManager pluginManager) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.persistence = persistence;
        type = new TaskFactoryType(log);
        pluginManager.addPluginListener(this, true);
    }

    public TaskFactoryType getType() {
        return type;
    }

    public ServerRealCommand createAddTaskCommand(String commandId, String commandName, String commandDescription,
                                                  final ServerRealTaskOwner owner,
                                                  final ServerRealList<TaskData, ServerRealTask> list) {
        return new ServerRealCommand(log, listenersFactory, commandId, commandName, commandDescription, Arrays.asList(
                new ServerRealParameter<String>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new ServerRealParameter<String>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new ServerRealParameter<Factory.Entry<ServerTaskFactory<?>>>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                ServerRealTask task = createTask(values, owner);
                // todo process annotations
                list.add(task);
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = task.getId();
                persistence.saveValues(path, values);
            }
        };
    }

    public ServerRealTask createTask(TypeInstanceMap values, ServerRealTaskOwner owner) throws HousemateException {
        TypeInstances taskType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(taskType == null || taskType.getFirstValue() == null)
            throw new HousemateException("No task type specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No task name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No task description specified");
        Factory.Entry<ServerTaskFactory<?>> taskFactoryEntry = type.deserialise(taskType.getElements().get(0));
        if(taskFactoryEntry == null)
            throw new HousemateException("No factory known for task type " + taskType);
        return taskFactoryEntry.getInjector().getInstance(taskFactoryEntry.getFactoryKey())
                .create(new TaskData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        Set<Factory.Entry<ServerTaskFactory<?>>> factoryEntries = Factory.getEntries(log, pluginInjector, ServerTaskFactory.class);
        for(Factory.Entry<ServerTaskFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new task factory for type " + factoryEntry.getTypeInfo().id());
            this.factoryEntries.put(factoryEntry.getTypeInfo().id(), factoryEntry);
            type.getOptions().add(new RealOption(log, listenersFactory, factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description()));
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        for(ServerTaskFactory<?> factory : pluginInjector.getInstance(new Key<Set<ServerTaskFactory<?>>>() {})) {
            TypeInfo factoryInformation = factory.getClass().getAnnotation(TypeInfo.class);
            if(factoryInformation != null) {
                factoryEntries.remove(factoryInformation.id());
                type.getOptions().remove(factoryInformation.id());
            }
        }
    }

    private class TaskFactoryType extends RealChoiceType<Factory.Entry<ServerTaskFactory<?>>> {
        protected TaskFactoryType(Log log) {
            super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(Factory.Entry<ServerTaskFactory<?>> entry) {
            return new TypeInstance(entry.getTypeInfo().id());
        }

        @Override
        public Factory.Entry<ServerTaskFactory<?>> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factoryEntries.get(value.getValue()) : null;
        }
    }
}