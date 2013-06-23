package com.intuso.housemate.broker.factory;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealParameter;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 21/08/12
 * Time: 12:28
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

    private final BrokerGeneralResources resources;
    private final Map<String, BrokerConditionFactory<?>> factories;
    private final ConditionFactoryType type;

    public ConditionFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, BrokerConditionFactory<?>>();
        type = new ConditionFactoryType(resources.getClientResources());
        resources.addPluginListener(this, true);
    }

    public ConditionFactoryType getType() {
        return type;
    }

    public BrokerRealCommand createAddConditionCommand(String commandId, String commandName, String commandDescription, final BrokerRealList<ConditionWrappable, BrokerRealCondition> list) {
        return new BrokerRealCommand(resources.getRealResources(), commandId, commandName, commandDescription, Arrays.asList(
                new BrokerRealParameter<String>(resources.getRealResources(), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealParameter<String>(resources.getRealResources(), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealParameter<BrokerConditionFactory<?>>(resources.getRealResources(), TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                BrokerRealCondition condition = createCondition(values);
                list.add(condition);
                resources.getStorage().saveValues(list.getPath(), condition.getId(), values);
            }
        };
    }

    public BrokerRealCondition createCondition(TypeInstances values) throws HousemateException {
        TypeInstance conditionType = values.get(TYPE_PARAMETER_ID);
        if(conditionType == null && conditionType.getValue() != null)
            throw new HousemateException("No condition type specified");
        TypeInstance name = values.get(NAME_PARAMETER_ID);
        if(name == null && name.getValue() != null)
            throw new HousemateException("No condition name specified");
        TypeInstance description = values.get(DESCRIPTION_PARAMETER_ID);
        if(description == null && description.getValue() != null)
            throw new HousemateException("No condition description specified");
        BrokerConditionFactory<?> conditionFactory = type.deserialise(conditionType);
        if(conditionFactory == null)
            throw new HousemateException("No factory known for condition type " + conditionType);
        return conditionFactory.create(resources.getRealResources(), name.getValue(), name.getValue(), description.getValue());
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(BrokerConditionFactory<?> factory : plugin.getConditionFactories()) {
            resources.getLog().d("Adding new condition factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(resources.getClientResources(), factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(BrokerConditionFactory<?> factory : plugin.getConditionFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class ConditionFactoryType extends RealSingleChoiceType<BrokerConditionFactory<?>> {
        protected ConditionFactoryType(RealResources resources) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(BrokerConditionFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public BrokerConditionFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}