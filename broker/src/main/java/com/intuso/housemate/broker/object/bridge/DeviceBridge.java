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
 */
public class DeviceBridge
        extends PrimaryObjectBridge<
            DeviceWrappable,
            DeviceBridge,
            DeviceListener<? super DeviceBridge>>
        implements Device<
            CommandBridge,
            CommandBridge,
            CommandBridge,
            ListBridge<CommandWrappable, Command<?, ?>, CommandBridge>,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ListBridge<ValueWrappable, Value<?, ?>, ValueBridge>,
            PropertyBridge,
            ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>,
            DeviceBridge> {

    private ListBridge<CommandWrappable, Command<?, ?>, CommandBridge> commandList;
    private ListBridge<ValueWrappable, Value<?, ?>, ValueBridge> valueList;
    private ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> propertyList;

    public DeviceBridge(BrokerBridgeResources resources, Device<?, ?, ? extends Command<?, ?>, ?, ?, ?, ?, ? extends Value<?, ?>, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        super(resources, new DeviceWrappable(device.getId(), device.getName(), device.getDescription()), device);
        commandList = new ListBridge<CommandWrappable, Command<?, ?>, CommandBridge>(resources, device.getCommands(), new CommandBridge.Converter(resources));
        valueList = new ListBridge<ValueWrappable, Value<?, ?>, ValueBridge>(resources, device.getValues(), new ValueBridge.Converter(resources));
        propertyList = new ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>(resources, device.getProperties(), new PropertyBridge.Converter(resources));
        addWrapper(commandList);
        addWrapper(valueList);
        addWrapper(propertyList);
    }

    @Override
    public ListBridge<CommandWrappable, Command<?, ?>, CommandBridge> getCommands() {
        return commandList;
    }

    @Override
    public ListBridge<ValueWrappable, Value<?, ?>, ValueBridge> getValues() {
        return valueList;
    }

    @Override
    public ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public final static class Converter implements Function<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public DeviceBridge apply(@Nullable Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device) {
            return new DeviceBridge(resources, device);
        }
    }
}
