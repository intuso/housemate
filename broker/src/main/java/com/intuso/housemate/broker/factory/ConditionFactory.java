package com.intuso.housemate.broker.factory;

import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.real.BrokerRealArgument;
import com.intuso.housemate.broker.object.real.BrokerRealCommand;
import com.intuso.housemate.broker.object.real.BrokerRealList;
import com.intuso.housemate.broker.object.real.condition.BrokerRealCondition;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.type.TypeSerialiser;
import com.intuso.housemate.core.object.condition.ConditionWrappable;
import com.intuso.housemate.real.RealOption;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.real.impl.type.StringType;

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

    public final static String NAME_ARGUMENT_ID = "name";
    public final static String NAME_ARGUMENT_NAME = "Name";
    public final static String NAME_ARGUMENT_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_ARGUMENT_ID = "description";
    public final static String DESCRIPTION_ARGUMENT_NAME = "Description";
    public final static String DESCRIPTION_ARGUMENT_DESCRIPTION = "Description for the new condition";
    public final static String TYPE_ARGUMENT_ID = "type";
    public final static String TYPE_ARGUMENT_NAME = "Type";
    public final static String TYPE_ARGUMENT_DESCRIPTION = "The type of the new condition";

    private final BrokerGeneralResources resources;
    private final Map<String, BrokerConditionFactory<?>> factories;
    private final ConditionFactoryType type;
    private final ConditionFactorySerialiser serialiser;

    public ConditionFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, BrokerConditionFactory<?>>();
        serialiser = new ConditionFactorySerialiser();
        type = new ConditionFactoryType(resources.getClientResources(), serialiser);
        resources.addPluginListener(this, true);
    }

    public ConditionFactoryType getType() {
        return type;
    }

    public BrokerRealCommand createAddConditionCommand(String commandId, String commandName, String commandDescription, final BrokerRealList<ConditionWrappable, BrokerRealCondition> list) {
        return new BrokerRealCommand(resources.getRealResources(), commandId, commandName, commandDescription, Arrays.asList(
                new BrokerRealArgument<String>(resources.getRealResources(), NAME_ARGUMENT_ID, NAME_ARGUMENT_NAME, NAME_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<String>(resources.getRealResources(), DESCRIPTION_ARGUMENT_ID, DESCRIPTION_ARGUMENT_NAME, DESCRIPTION_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<BrokerConditionFactory<?>>(resources.getRealResources(), TYPE_ARGUMENT_ID, TYPE_ARGUMENT_NAME, TYPE_ARGUMENT_DESCRIPTION, type)
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                BrokerRealCondition condition = createCondition(values);
                list.add(condition);
                resources.getStorage().watchPropertyValues(condition.getProperties());
                resources.getStorage().saveDetails(list.getPath(), condition.getId(), values);
            }
        };
    }

    public BrokerRealCondition createCondition(Map<String, String> values) throws HousemateException {
        String type = values.get(TYPE_ARGUMENT_ID);
        if(type == null)
            throw new HousemateException("No condition type specified");
        String name = values.get(NAME_ARGUMENT_ID);
        if(name == null)
            throw new HousemateException("No condition name specified");
        String description = values.get(DESCRIPTION_ARGUMENT_ID);
        if(description == null)
            throw new HousemateException("No condition description specified");
        BrokerConditionFactory<?> conditionFactory = serialiser.deserialise(type);
        if(conditionFactory == null)
            throw new HousemateException("No factory known for condition type " + type);
        return conditionFactory.create(resources.getRealResources(), name, name, description);
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
        protected ConditionFactoryType(RealResources resources, ConditionFactorySerialiser serialiser) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList(), serialiser);
        }
    }

    private class ConditionFactorySerialiser implements TypeSerialiser<BrokerConditionFactory<?>> {
        @Override
        public String serialise(BrokerConditionFactory<?> o) {
            return o.getTypeId();
        }

        @Override
        public BrokerConditionFactory<?> deserialise(String value) {
            return factories.get(value);
        }
    }
}