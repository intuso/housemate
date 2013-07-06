package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueData;

import javax.annotation.Nullable;

/**
 */
public class DeviceBridge
        extends PrimaryObjectBridge<
        DeviceData,
            DeviceBridge,
            DeviceListener<? super DeviceBridge>>
        implements Device<
            CommandBridge,
            CommandBridge,
            CommandBridge,
            ListBridge<CommandData, Command<?, ?>, CommandBridge>,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ListBridge<ValueData, Value<?, ?>, ValueBridge>,
            PropertyBridge,
            ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            DeviceBridge> {

    private ListBridge<CommandData, Command<?, ?>, CommandBridge> commandList;
    private ListBridge<ValueData, Value<?, ?>, ValueBridge> valueList;
    private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public DeviceBridge(BrokerBridgeResources resources, Device<?, ?, ? extends Command<?, ?>, ?, ?, ?, ?, ? extends Value<?, ?>, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        super(resources, new DeviceData(device.getId(), device.getName(), device.getDescription()), device);
        commandList = new ListBridge<CommandData, Command<?, ?>, CommandBridge>(resources, device.getCommands(), new CommandBridge.Converter(resources));
        valueList = new ListBridge<ValueData, Value<?, ?>, ValueBridge>(resources, device.getValues(), new ValueBridge.Converter(resources));
        propertyList = new ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>(resources, device.getProperties(), new PropertyBridge.Converter(resources));
        addWrapper(commandList);
        addWrapper(valueList);
        addWrapper(propertyList);
    }

    @Override
    public ListBridge<CommandData, Command<?, ?>, CommandBridge> getCommands() {
        return commandList;
    }

    @Override
    public ListBridge<ValueData, Value<?, ?>, ValueBridge> getValues() {
        return valueList;
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
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
