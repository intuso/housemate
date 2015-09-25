package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyData, CommandData, CommandBridge, Property.Listener<? super PropertyBridge>, PropertyBridge>
        implements Property<TypeInstances, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(Log log, ListenersFactory listenersFactory, Property<?, ?, ?> property) {
        super(log, listenersFactory,
            new PropertyData(property.getId(), property.getName(), property.getDescription(), property.getTypeId(), (TypeInstances) property.getValue()),
            property);
        setCommand = new CommandBridge(log, listenersFactory, property.getSetCommand());
        addChild(setCommand);
    }

    @Override
    public void set(final TypeInstances value, Command.PerformListener<? super CommandBridge> listener) {
        TypeInstanceMap values = new TypeInstanceMap();
        values.getChildren().put(PropertyData.VALUE_PARAM, value);
        setCommand.perform(values, listener);
    }

    @Override
    public CommandBridge getSetCommand() {
        return setCommand;
    }

    public static class Converter implements Function<Property<?, ?, ?>, PropertyBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public PropertyBridge apply(Property<?, ?, ?> property) {
            return new PropertyBridge(log, listenersFactory, property);
        }
    }
}
