package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.Arrays;

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
                Arrays.<RealParameter<?>>asList(new RealParameter<O>(resources, VALUE_PARAM, VALUE_PARAM, "The new value for the property", type))) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                O object = getType().deserialise(values.get(VALUE_PARAM));
                RealProperty.this.setTypedValue(object);
            }
        };
        addWrapper(setCommand);
    }

    @Override
    public void set(final TypeInstance value, CommandListener<? super RealCommand> listener) {
        getSetCommand().perform(new TypeInstances() {
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
