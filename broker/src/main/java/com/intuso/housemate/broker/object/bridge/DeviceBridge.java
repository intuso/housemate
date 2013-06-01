package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class DeviceBridge
        extends PrimaryObjectBridge<DeviceWrappable, DeviceBridge, DeviceListener<? super DeviceBridge>>
        implements Device<CommandBridge, CommandBridge, CommandBridge, ListBridge<Command<?, ?>, CommandWrappable, CommandBridge>,
                    ValueBridge, ValueBridge, ValueBridge, ValueBridge, ListBridge<Value<?, ?>, ValueWrappable, ValueBridge>,
                    PropertyBridge, PropertyBridge, ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>, DeviceBridge> {

    private ListBridge<Command<?, ?>, CommandWrappable, CommandBridge> commandList;
    private ListBridge<Value<?, ?>, ValueWrappable, ValueBridge> valueList;
    private ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> propertyList;

    public DeviceBridge(BrokerBridgeResources resources, Device<?, ?, ? extends Command<?, ?>, ?, ?, ?, ?, ? extends Value<?, ?>, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        super(resources, new DeviceWrappable(device.getId(), device.getName(), device.getDescription()), device);
        commandList = new ListBridge<Command<?, ?>, CommandWrappable, CommandBridge>(resources, device.getCommands(), new CommandConverter(resources));
        valueList = new ListBridge<Value<?, ?>, ValueWrappable, ValueBridge>(resources, device.getValues(), new ValueConverter(resources));
        propertyList = new ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>(resources, device.getProperties(), new PropertyConverter(resources));
        addWrapper(commandList);
        addWrapper(valueList);
        addWrapper(propertyList);
    }

    @Override
    public ListBridge<Command<?, ?>, CommandWrappable, CommandBridge> getCommands() {
        return commandList;
    }

    @Override
    public ListBridge<Value<?, ?>, ValueWrappable, ValueBridge> getValues() {
        return valueList;
    }

    @Override
    public ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> getProperties() {
        return propertyList;
    }

    private class CommandConverter implements Function<Command<?, ?>, CommandBridge> {

        private final BrokerBridgeResources resources;

        private CommandConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public CommandBridge apply(@Nullable Command<?, ?> command) {
            return new CommandBridge(resources, command);
        }
    }

    private class ValueConverter implements Function<Value<?, ?>, ValueBridge> {

        private final BrokerBridgeResources resources;

        private ValueConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ValueBridge apply(@Nullable Value<?, ?> value) {
            return new ValueBridge(resources, value);
        }
    }

    private class PropertyConverter implements Function<Property<?, ?, ?>, PropertyBridge> {

        private final BrokerBridgeResources resources;

        private PropertyConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public PropertyBridge apply(@Nullable Property<?, ?, ?> property) {
            return new PropertyBridge(resources, property);
        }
    }
}
