package com.intuso.housemate.real;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.property.PropertyWrappable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class RealProperty<O>
        extends RealValueBase<PropertyWrappable, CommandWrappable, RealCommand, O, RealProperty<O>>
        implements Property<RealType<?, ?, O>, RealCommand, RealProperty<O>> {

    private RealCommand setCommand;

    public RealProperty(RealResources resources, String id, String name, String description, RealType<?, ?, O> type, O value) {
        super(resources, new PropertyWrappable(id, name, description, type.getId(), type.serialise(value)), type);
        setCommand = new RealCommand(resources, SET_COMMAND, SET_COMMAND, "The function to change the property's value",
                Arrays.<RealArgument<?>>asList(new RealArgument<O>(resources, VALUE_PARAM, VALUE_PARAM, "The new value for the property", type))) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                String newValueString = values.get(VALUE_PARAM);
                O newValue = newValueString == null ? null : getType().deserialise(newValueString);
                RealProperty.this.setTypedValue(newValue);
            }
        };
        addWrapper(setCommand);
    }

    @Override
    protected final RealProperty getThis() {
        return this;
    }

    @Override
    public void set(final String value, CommandListener<? super RealCommand> listener) {
        getSetCommand().perform(new HashMap<String, String>() {
            {
                put(VALUE, value);
            }
        }, listener);
    }

    @Override
    public RealCommand getSetCommand() {
        return setCommand;
    }
}
