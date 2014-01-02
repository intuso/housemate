package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;

/**
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyData, CommandData, CommandBridge,  PropertyBridge>
        implements Property<TypeBridge, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(BrokerBridgeResources resources, Property<?, ?, ?> property,
                          ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
        super(resources,
            new PropertyData(property.getId(), property.getName(), property.getDescription(), property.getType().getId(), property.getTypeInstances()),
            property, types);
        setCommand = new CommandBridge(resources, property.getSetCommand(), types);
        addChild(setCommand);
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
        private final ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types;

        public Converter(BrokerBridgeResources resources, ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
            this.resources = resources;
            this.types = types;
        }

        @Override
        public PropertyBridge apply(Property<?, ?, ?> property) {
            return new PropertyBridge(resources, property, types);
        }
    }
}
