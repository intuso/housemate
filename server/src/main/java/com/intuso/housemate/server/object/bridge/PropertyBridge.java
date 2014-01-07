package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.log.Log;

/**
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyData, CommandData, CommandBridge,  PropertyBridge>
        implements Property<TypeBridge, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(Log log, Property<?, ?, ?> property,
                          ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log,
            new PropertyData(property.getId(), property.getName(), property.getDescription(), property.getTypeId(), property.getTypeInstances()),
            property, types);
        setCommand = new CommandBridge(log, property.getSetCommand(), types);
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

        private final Log log;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.types = types;
        }

        @Override
        public PropertyBridge apply(Property<?, ?, ?> property) {
            return new PropertyBridge(log, property, types);
        }
    }
}
