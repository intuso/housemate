package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealProperty<O>
        extends RealValueBase<PropertyData, CommandData, RealCommand, O, RealProperty<O>>
        implements Property<RealType<?, ?, O>, RealCommand, RealProperty<O>> {

    private RealCommand setCommand;

    /**
     * @param log {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(Log log, String id, String name, String description, RealType<?, ?, O> type,
                        O ... values) {
        this(log, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(Log log, String id, String name, String description, RealType<?, ?, O> type,
                        List<O> values) {
        super(log, new PropertyData(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
        setCommand = new RealCommand(log, SET_COMMAND_ID, SET_COMMAND_ID, "The function to change the property's value",
                Arrays.<RealParameter<?>>asList(new RealParameter<O>(log, VALUE_PARAM, VALUE_PARAM, "The new value for the property", type))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                List<O> typedValues = RealType.deserialiseAll(getType(), values.get(VALUE_PARAM));
                RealProperty.this.setTypedValues(typedValues);
            }
        };
        addChild(setCommand);
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
