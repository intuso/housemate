package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class DeviceBridge
        extends BridgeObject<DeviceData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, DeviceBridge, Device.Listener<? super DeviceBridge>>
        implements Device<
        CommandBridge,
        CommandBridge,
        CommandBridge,
        ValueBridge,
        ValueBridge,
        PropertyBridge,
        ValueBridge,
        ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
        ConvertingListBridge<FeatureData, Feature<?, ?, ?>, FeatureBridge>,
        DeviceBridge> {

    private Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device;
    private CommandBridge renameCommand;
    private CommandBridge removeCommand;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;
    private PropertyBridge driverProperty;
    private ValueBridge driverLoadedValue;
    private ConvertingListBridge<CommandData, Command<?, ?, ?, ?>, CommandBridge> commandList;
    private ConvertingListBridge<ValueData, Value<?, ?>, ValueBridge> valueList;
    private ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;
    private ConvertingListBridge<FeatureData, Feature<?, ?, ?>, FeatureBridge> featureList;

    public DeviceBridge(Log log, ListenersFactory listenersFactory,
                        Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device) {
        super(log, listenersFactory, new DeviceData(device.getId(), device.getName(), device.getDescription()));
        this.device = device;
        renameCommand = new CommandBridge(log, listenersFactory, device.getRenameCommand());
        removeCommand = new CommandBridge(log, listenersFactory, device.getRemoveCommand());
        runningValue = new ValueBridge(log, listenersFactory, device.getRunningValue());
        startCommand = new CommandBridge(log, listenersFactory, device.getStartCommand());
        stopCommand = new CommandBridge(log, listenersFactory, device.getStopCommand());
        errorValue = new ValueBridge(log, listenersFactory, device.getErrorValue());
        driverProperty = new PropertyBridge(log, listenersFactory, device.getDriverProperty());
        driverLoadedValue = new ValueBridge(log, listenersFactory, device.getDriverLoadedValue());
        propertyList = new ConvertingListBridge<>(log, listenersFactory, (com.intuso.housemate.object.api.internal.List<? extends Property<?, ?, ?>>) device.getProperties(), new PropertyBridge.Converter(log, listenersFactory));
        featureList = new ConvertingListBridge<>(log, listenersFactory, (com.intuso.housemate.object.api.internal.List<? extends Feature<?, ?, ?>>) device.getFeatures(), new FeatureBridge.Converter(log, listenersFactory));
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(commandList);
        addChild(valueList);
        addChild(propertyList);
        addChild(featureList);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(device.addObjectListener(new Device.Listener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>() {

            @Override
            public void renamed(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, String oldName, String newName) {
                for(Device.Listener<? super DeviceBridge> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
                getData().setName(newName);
                broadcastMessage(DeviceData.NEW_NAME, new StringPayload(newName));
            }

            @Override
            public void error(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, String error) {

            }

            @Override
            public void running(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, boolean running) {

            }

            @Override
            public void driverLoaded(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, boolean loaded) {

            }
        }));
        return result;
    }

    @Override
    public CommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public CommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public CommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public PropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ConvertingListBridge<FeatureData, Feature<?, ?, ?>, FeatureBridge> getFeatures() {
        return featureList;
    }

    public final static class Converter implements Function<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public DeviceBridge apply(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device) {
            return new DeviceBridge(log, listenersFactory, device);
        }
    }
}
