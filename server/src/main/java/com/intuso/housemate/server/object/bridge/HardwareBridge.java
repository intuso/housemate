package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.housemate.object.api.internal.Hardware;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class HardwareBridge
        extends BridgeObject<
        HardwareData,
        HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            HardwareBridge,
            Hardware.Listener<? super HardwareBridge>>
        implements Hardware<
        CommandBridge,
        CommandBridge,
        CommandBridge,
        ValueBridge,
        ValueBridge,
        PropertyBridge,
        ValueBridge,
        ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            HardwareBridge> {

    private final Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware;
    private CommandBridge renameCommand;
    private CommandBridge removeCommand;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;
    private PropertyBridge driverProperty;
    private ValueBridge driverLoadedValue;
    private ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public HardwareBridge(Log log, ListenersFactory listenersFactory, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware) {
        super(log, listenersFactory, new HardwareData(hardware.getId(), hardware.getName(), hardware.getDescription()));
        this.hardware = hardware;
        renameCommand = new CommandBridge(log, listenersFactory, hardware.getRenameCommand());
        removeCommand = new CommandBridge(log, listenersFactory, hardware.getRemoveCommand());
        runningValue = new ValueBridge(log, listenersFactory, hardware.getRunningValue());
        startCommand = new CommandBridge(log, listenersFactory, hardware.getStartCommand());
        stopCommand = new CommandBridge(log, listenersFactory, hardware.getStopCommand());
        errorValue = new ValueBridge(log, listenersFactory, hardware.getErrorValue());
        driverProperty = new PropertyBridge(log, listenersFactory, hardware.getDriverProperty());
        driverLoadedValue = new ValueBridge(log, listenersFactory, hardware.getDriverLoadedValue());
        propertyList = new ConvertingListBridge<>(log, listenersFactory, hardware.getProperties(),
                new PropertyBridge.Converter(log, listenersFactory));
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(propertyList);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(hardware.addObjectListener(new Hardware.Listener<Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>>() {

            @Override
            public void renamed(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> device, String oldName, String newName) {
                for(Hardware.Listener<? super HardwareBridge> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
                getData().setName(newName);
                broadcastMessage(HardwareData.NEW_NAME, new StringPayload(newName));
            }

            @Override
            public void error(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware, String error) {

            }

            @Override
            public void running(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware, boolean running) {

            }

            @Override
            public void driverLoaded(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware, boolean loaded) {

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

    public static class Converter implements Function<Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public HardwareBridge apply(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            return new HardwareBridge(log, listenersFactory, hardware);
        }
    }
}
