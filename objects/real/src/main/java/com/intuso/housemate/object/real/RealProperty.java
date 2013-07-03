package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealProperty<O>
        extends RealValueBase<PropertyWrappable, CommandWrappable, RealCommand, O, RealProperty<O>>
        implements Property<RealType<?, ?, O>, RealCommand, RealProperty<O>> {

    private RealCommand setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(RealResources resources, String id, String name, String description, RealType<?, ?, O> type,
                        O ... values) {
        this(resources, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(RealResources resources, String id, String name, String description, RealType<?, ?, O> type,
                        List<O> values) {
        super(resources, new PropertyWrappable(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
        setCommand = new RealCommand(resources, SET_COMMAND_ID, SET_COMMAND_ID, "The function to change the property's value",
                Arrays.<RealParameter<?>>asList(new RealParameter<O>(resources, VALUE_PARAM, VALUE_PARAM, "The new value for the property", type))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                List<O> typedValues = RealType.deserialiseAll(getType(), values.get(VALUE_PARAM));
                RealProperty.this.setTypedValues(typedValues);
            }
        };
        addWrapper(setCommand);
    }

    @Override
    public void set(final TypeInstances values, CommandListener<? super RealCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                put(VALUE_ID, values);
            }
        }, listener);
    }

    @Override
    public RealCommand getSetCommand() {
        return setCommand;
    }
}
