package com.intuso.housemate.server.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 */
public final class ConditionFactory implements PluginListener {

    public final static String TYPE_ID = "condition-factory";
    public final static String TYPE_NAME = "Condition Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new conditions";

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "Description for the new condition";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new condition";

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Persistence persistence;
    
    private final Map<String, Factory.Entry<ServerConditionFactory<?>>> factoryEntries = Maps.newHashMap();
    private final ConditionFactoryType type;

    @Inject
    public ConditionFactory(Log log, ListenersFactory listenersFactory, Persistence persistence, PluginManager pluginManager) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.persistence = persistence;
        type = new ConditionFactoryType(log);
        pluginManager.addPluginListener(this, true);
    }

    public ConditionFactoryType getType() {
        return type;
    }

    public ServerRealCommand createAddConditionCommand(String commandId, String commandName, String commandDescription,
                                                       final ServerRealConditionOwner owner,
                                                       final ServerRealList<ConditionData, ServerRealCondition> list) {
        return new ServerRealCommand(log, listenersFactory, commandId, commandName, commandDescription, Arrays.asList(
                new ServerRealParameter<String>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new ServerRealParameter<String>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new ServerRealParameter<Factory.Entry<ServerConditionFactory<?>>>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                ServerRealCondition condition = createCondition(values, owner);
                // todo process annotations
                list.add(condition);
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = condition.getId();
                persistence.saveValues(path, values);
            }
        };
    }

    public ServerRealCondition createCondition(TypeInstanceMap values, ServerRealConditionOwner owner) throws HousemateException {
        TypeInstances conditionType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(conditionType == null || conditionType.getFirstValue() == null)
            throw new HousemateException("No condition type specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No condition name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No condition description specified");
        Factory.Entry<ServerConditionFactory<?>> conditionFactoryEntry = type.deserialise(conditionType.getElements().get(0));
        if(conditionFactoryEntry == null)
            throw new HousemateException("No factory known for condition type " + conditionType);
        return conditionFactoryEntry.getInjector().getInstance(conditionFactoryEntry.getFactoryKey())
                .create(new ConditionData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        Set<Factory.Entry<ServerConditionFactory<?>>> factoryEntries = Factory.getEntries(log, pluginInjector, ServerConditionFactory.class);
        for(Factory.Entry<ServerConditionFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new condition factory for type " + factoryEntry.getTypeInfo().id());
            this.factoryEntries.put(factoryEntry.getTypeInfo().id(), factoryEntry);
            type.getOptions().add(new RealOption(log, listenersFactory, factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description()));
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        for(ServerConditionFactory<?> factory : pluginInjector.getInstance(new Key<Set<ServerConditionFactory<?>>>() {})) {
            TypeInfo factoryInformation = factory.getClass().getAnnotation(TypeInfo.class);
            if(factoryInformation != null) {
                factoryEntries.remove(factoryInformation.id());
                type.getOptions().remove(factoryInformation.id());
            }
        }
    }

    private class ConditionFactoryType extends RealChoiceType<Factory.Entry<ServerConditionFactory<?>>> {
        protected ConditionFactoryType(Log log) {
            super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(Factory.Entry<ServerConditionFactory<?>> entry) {
            return new TypeInstance(entry.getTypeInfo().id());
        }

        @Override
        public Factory.Entry<ServerConditionFactory<?>> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factoryEntries.get(value.getValue()) : null;
        }
    }
}