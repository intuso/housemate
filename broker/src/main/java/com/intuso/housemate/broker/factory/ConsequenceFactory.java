package com.intuso.housemate.broker.factory;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealArgument;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;
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

    public ConsequenceFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, BrokerConsequenceFactory<?>>();
        type = new ConsequenceFactoryType(resources.getClientResources());
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
            public void perform(TypeInstances values) throws HousemateException {
                BrokerRealConsequence consequence = createConsequence(values);
                list.add(consequence);
                resources.getStorage().saveValues(list.getPath(), consequence.getId(), values);
            }
        };
    }

    public BrokerRealConsequence createConsequence(TypeInstances values) throws HousemateException {
        TypeInstance consequenceType = values.get(TYPE_ARGUMENT_ID);
        if(consequenceType == null && consequenceType.getValue() != null)
            throw new HousemateException("No consequence type specified");
        TypeInstance name = values.get(NAME_ARGUMENT_ID);
        if(name == null && name.getValue() != null)
            throw new HousemateException("No consequence name specified");
        TypeInstance description = values.get(DESCRIPTION_ARGUMENT_ID);
        if(description == null && description.getValue() != null)
            throw new HousemateException("No consequence description specified");
        BrokerConsequenceFactory<?> consequenceFactory = type.deserialise(consequenceType);
        if(consequenceFactory == null)
            throw new HousemateException("No factory known for consequence type " + consequenceType);
        return consequenceFactory.create(resources.getRealResources(), name.getValue(), name.getValue(), description.getValue());
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
        protected ConsequenceFactoryType(RealResources resources) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(BrokerConsequenceFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public BrokerConsequenceFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}