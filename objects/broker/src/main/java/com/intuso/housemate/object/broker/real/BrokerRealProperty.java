package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property
 */
public class BrokerRealProperty<O>
        extends BrokerRealValueBase<PropertyData, CommandData, BrokerRealCommand, O, BrokerRealProperty<O>>
        implements Property<RealType<?, ?, O>, BrokerRealCommand, BrokerRealProperty<O>> {

    private BrokerRealCommand setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the property
     * @param values the initial values of the property
     */
    public BrokerRealProperty(BrokerRealResources resources, String id, String name, String description,
                              RealType<?, ?, O> type, O ... values) {
        this(resources, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the property
     * @param values the initial values of the property
     */
    public BrokerRealProperty(BrokerRealResources resources, String id, String name, String description,
                              RealType<?, ?, O> type, List<O> values) {
        super(resources, new PropertyData(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
        setCommand = new BrokerRealCommand(resources, SET_COMMAND_ID, SET_COMMAND_ID, "The command to change the property's values",
                Arrays.<BrokerRealParameter<?>>asList(new BrokerRealParameter<O>(resources, VALUE_PARAM, VALUE_PARAM, "The new values for the property", type))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                List<O> objects = RealType.deserialiseAll(getType(), values.get(VALUE_PARAM));
                BrokerRealProperty.this.setTypedValues(objects);
            }
        };
        addWrapper(setCommand);
    }

    @Override
    public void set(final TypeInstances value, CommandListener<? super BrokerRealCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                put(VALUE_ID, value);
            }
        }, listener);
    }

    @Override
    public BrokerRealCommand getSetCommand() {
        return setCommand;
    }
}
