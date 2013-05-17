package com.intuso.housemate.broker.factory;

import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.real.BrokerRealArgument;
import com.intuso.housemate.broker.object.real.BrokerRealCommand;
import com.intuso.housemate.broker.object.real.BrokerRealList;
import com.intuso.housemate.broker.object.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.type.TypeSerialiser;
import com.intuso.housemate.core.object.consequence.ConsequenceWrappable;
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
public final class ConsequenceFactory implements PluginListener {

    public final static String TYPE_ID = "consequence-factory";
    public final static String TYPE_NAME = "Consequence Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new consequences";

    public final static String NAME_ARGUMENT_ID = "name";
    public final static String NAME_ARGUMENT_NAME = "Name";
    public final static String NAME_ARGUMENT_DESCRIPTION = "The name of the new consequence";
    public final static String DESCRIPTION_ARGUMENT_ID = "description";
    public final static String DESCRIPTION_ARGUMENT_NAME = "Description";
    public final static String DESCRIPTION_ARGUMENT_DESCRIPTION = "Description for the new consequence";
    public final static String TYPE_ARGUMENT_ID = "type";
    public final static String TYPE_ARGUMENT_NAME = "Type";
    public final static String TYPE_ARGUMENT_DESCRIPTION = "The type of the new consequence";

    private final BrokerGeneralResources resources;
    private final Map<String, BrokerConsequenceFactory<?>> factories;
    private final ConsequenceFactoryType type;
    private final ConsequenceFactorySerialiser serialiser;

    public ConsequenceFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, BrokerConsequenceFactory<?>>();
        serialiser = new ConsequenceFactorySerialiser();
        type = new ConsequenceFactoryType(resources.getClientResources(), serialiser);
        resources.addPluginListener(this, true);
    }

    public ConsequenceFactoryType getType() {
        return type;
    }

    public BrokerRealCommand createAddConsequenceCommand(String commandId, String commandName, String commandDescription, final BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> list) {
        return new BrokerRealCommand(resources.getRealResources(), commandId, commandName, commandDescription, Arrays.asList(
                new BrokerRealArgument<String>(resources.getRealResources(), NAME_ARGUMENT_ID, NAME_ARGUMENT_NAME, NAME_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<String>(resources.getRealResources(), DESCRIPTION_ARGUMENT_ID, DESCRIPTION_ARGUMENT_NAME, DESCRIPTION_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<BrokerConsequenceFactory<?>>(resources.getRealResources(), TYPE_ARGUMENT_ID, TYPE_ARGUMENT_NAME, TYPE_ARGUMENT_DESCRIPTION, type)
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                BrokerRealConsequence consequence = createConsequence(values);
                list.add(consequence);
                resources.getStorage().watchPropertyValues(consequence.getProperties());
                resources.getStorage().saveDetails(list.getPath(), consequence.getId(), values);
            }
        };
    }

    public BrokerRealConsequence createConsequence(Map<String, String> values) throws HousemateException {
        String type = values.get(TYPE_ARGUMENT_ID);
        if(type == null)
            throw new HousemateException("No consequence type specified");
        String name = values.get(NAME_ARGUMENT_ID);
        if(name == null)
            throw new HousemateException("No consequence name specified");
        String description = values.get(DESCRIPTION_ARGUMENT_ID);
        if(description == null)
            throw new HousemateException("No consequence description specified");
        BrokerConsequenceFactory<?> consequenceFactory = serialiser.deserialise(type);
        if(consequenceFactory == null)
            throw new HousemateException("No factory known for consequence type " + type);
        return consequenceFactory.create(resources.getRealResources(), name, name, description);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(BrokerConsequenceFactory<?> factory : plugin.getConsequenceFactories()) {
            resources.getLog().d("Adding new consequence factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(resources.getClientResources(), factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(BrokerConsequenceFactory<?> factory : plugin.getConsequenceFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class ConsequenceFactoryType extends RealSingleChoiceType<BrokerConsequenceFactory<?>> {
        protected ConsequenceFactoryType(RealResources resources, ConsequenceFactorySerialiser serialiser) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList(), serialiser);
        }
    }

    private class ConsequenceFactorySerialiser implements TypeSerialiser<BrokerConsequenceFactory<?>> {
        @Override
        public String serialise(BrokerConsequenceFactory<?> o) {
            return o.getTypeId();
        }

        @Override
        public BrokerConsequenceFactory<?> deserialise(String value) {
            return factories.get(value);
        }
    }
}