package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.property.PropertyWrappable;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:50
 * To change this template use File | Settings | File Templates.
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyWrappable, CommandWrappable, CommandBridge,  PropertyBridge>
        implements Property<TypeBridge, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(BrokerBridgeResources resources, Property<?, ?, ?> property) {
        super(resources,
            new PropertyWrappable(property.getId(), property.getName(), property.getDescription(), property.getType().getId(), property.getValue()),
            property);
        setCommand = new CommandBridge(resources, property.getSetCommand());
        addWrapper(setCommand);
    }

    @Override
    public void set(final String value, CommandListener<? super CommandBridge> listener) throws HousemateException {
        setCommand.perform(new HashMap<String, String>() {
            {
                put(VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public CommandBridge getSetCommand() {
        return setCommand;
    }
}
