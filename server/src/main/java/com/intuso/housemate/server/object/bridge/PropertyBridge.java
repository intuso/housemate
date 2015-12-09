package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class PropertyBridge
        extends ValueBridgeBase<PropertyData, CommandData, CommandBridge, Property.Listener<? super PropertyBridge>, PropertyBridge>
        implements Property<TypeInstances, CommandBridge, PropertyBridge> {

    private CommandBridge setCommand;

    public PropertyBridge(Logger logger, ListenersFactory listenersFactory, Property<?, ?, ?> property) {
        super(logger, listenersFactory,
            new PropertyData(property.getId(), property.getName(), property.getDescription(), property.getTypeId(), (TypeInstances) property.getValue()),
            property);
        setCommand = new CommandBridge(logger, listenersFactory, property.getSetCommand());
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

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public PropertyBridge apply(Property<?, ?, ?> property) {
            return new PropertyBridge(logger, listenersFactory, property);
        }
    }
}
