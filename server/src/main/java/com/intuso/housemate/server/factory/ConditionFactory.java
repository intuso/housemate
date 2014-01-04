package com.intuso.housemate.server.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.storage.Storage;

import java.util.Arrays;
import java.util.Map;

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

    private final ServerRealResources serverRealResources;
    private final RealResources realResources;
    private final Storage storage;
    
    private final Map<String, ServerConditionFactory<?>> factories = Maps.newHashMap();
    private final ConditionFactoryType type;

    @Inject
    public ConditionFactory(ServerRealResources serverRealResources, RealResources realResources,
                            Storage storage, PluginManager pluginManager) {
        this.serverRealResources = serverRealResources;
        this.realResources = realResources;
        this.storage = storage;
        type = new ConditionFactoryType(realResources);
        pluginManager.addPluginListener(this, true);
    }

    public ConditionFactoryType getType() {
        return type;
    }

    public ServerRealCommand createAddConditionCommand(String commandId, String commandName, String commandDescription,
                                                       final ServerRealConditionOwner owner,
                                                       final ServerRealList<ConditionData, ServerRealCondition> list) {
        return new ServerRealCommand(serverRealResources, commandId, commandName, commandDescription, Arrays.asList(
                new ServerRealParameter<String>(serverRealResources, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(realResources)),
                new ServerRealParameter<String>(serverRealResources, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(realResources)),
                new ServerRealParameter<ServerConditionFactory<?>>(serverRealResources, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                ServerRealCondition condition = createCondition(values, owner);
                // todo process annotations
                list.add(condition);
                storage.saveValues(list.getPath(), condition.getId(), values);
            }
        };
    }

    public ServerRealCondition createCondition(TypeInstanceMap values, ServerRealConditionOwner owner) throws HousemateException {
        TypeInstances conditionType = values.get(TYPE_PARAMETER_ID);
        if(conditionType == null || conditionType.getFirstValue() == null)
            throw new HousemateException("No condition type specified");
        TypeInstances name = values.get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No condition name specified");
        TypeInstances description = values.get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No condition description specified");
        ServerConditionFactory<?> conditionFactory = type.deserialise(conditionType.get(0));
        if(conditionFactory == null)
            throw new HousemateException("No factory known for condition type " + conditionType);
        return conditionFactory.create(serverRealResources, name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), owner);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(ServerConditionFactory<?> factory : plugin.getConditionFactories()) {
            realResources.getLog().d("Adding new condition factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(realResources, factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(ServerConditionFactory<?> factory : plugin.getConditionFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class ConditionFactoryType extends RealChoiceType<ServerConditionFactory<?>> {
        protected ConditionFactoryType(RealResources resources) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(ServerConditionFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public ServerConditionFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}