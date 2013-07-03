package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

import javax.annotation.Nullable;

/**
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyWrappable, CommandWrappable, CommandBridge,  PropertyBridge>
        implements Property<TypeBridge, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(BrokerBridgeResources resources, Property<?, ?, ?> property) {
        super(resources,
            new PropertyWrappable(property.getId(), property.getName(), property.getDescription(), property.getType().getId(), property.getTypeInstances()),
            property);
        setCommand = new CommandBridge(resources, property.getSetCommand());
        addWrapper(setCommand);
    }

    @Override
    public void set(final TypeInstances value, CommandListener<? super CommandBridge> listener) {
        setCommand.perform(new TypeInstanceMap() {
            {
                put(VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public CommandBridge getSetCommand() {
        return setCommand;
    }

    public static class Converter implements Function<Property<?, ?, ?>, PropertyBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public PropertyBridge apply(@Nullable Property<?, ?, ?> property) {
            return new PropertyBridge(resources, property);
        }
    }
}
