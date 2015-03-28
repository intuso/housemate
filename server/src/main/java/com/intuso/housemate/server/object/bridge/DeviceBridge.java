package com.intuso.housemate.server.object.bridge;

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
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

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
            CommandBridge,
            ListBridge<CommandData, Command<?, ?, ?>, CommandBridge>,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ValueBridge,
            ListBridge<ValueData, Value<?, ?>, ValueBridge>,
            PropertyBridge,
            ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            DeviceBridge> {

    private ListBridge<CommandData, Command<?, ?, ?>, CommandBridge> commandList;
    private ListBridge<ValueData, Value<?, ?>, ValueBridge> valueList;
    private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;
    private ValueBridge connectedValue;

    public DeviceBridge(Log log, ListenersFactory listenersFactory,
                        Device<?, ?, ?, ? extends Command<?, ?, ?>, ?, ?, ?, ?, ? extends Value<?, ?>, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        super(log, listenersFactory,
                new DeviceData(device.getId(), device.getName(), device.getDescription(), device.getFeatureIds(),
                        device.getCustomCommandIds(), device.getCustomValueIds(), device.getCustomPropertyIds()),
                device);
        commandList = new SingleListBridge<>(log, listenersFactory, device.getCommands(), new CommandBridge.Converter(log, listenersFactory));
        valueList = new SingleListBridge<>(log, listenersFactory, device.getValues(), new ValueBridge.Converter(log, listenersFactory));
        propertyList = new SingleListBridge<>(log, listenersFactory, device.getProperties(), new PropertyBridge.Converter(log, listenersFactory));
        connectedValue = new ValueBridge(log, listenersFactory, device.getConnectedValue());
        addChild(commandList);
        addChild(valueList);
        addChild(propertyList);
        addChild(connectedValue);
    }

    @Override
    public ListBridge<CommandData, Command<?, ?, ?>, CommandBridge> getCommands() {
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

    @Override
    public boolean isConnected() {
        List<Boolean> connecteds = RealType.deserialiseAll(BooleanType.SERIALISER, connectedValue.getTypeInstances());
        return connecteds != null && connecteds.size() > 0 && connecteds.get(0) != null ? connecteds.get(0) : false;
    }

    @Override
    public ValueBridge getConnectedValue() {
        return connectedValue;
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }

    public final static class Converter implements Function<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public DeviceBridge apply(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device) {
            return new DeviceBridge(log, listenersFactory, device);
        }
    }
}
