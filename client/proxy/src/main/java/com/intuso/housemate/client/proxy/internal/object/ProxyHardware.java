package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyHardware<
        COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUE extends ProxyValue<?, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        PROPERTY extends ProxyProperty<?, ?, ?>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        DEVICES extends ProxyList<? extends ProxyDeviceConnected<?, ?, ?, ?>, ?>,
        HARDWARE extends ProxyHardware<COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICES, HARDWARE>>
        extends ProxyObject<Hardware.Data, Hardware.Listener<? super HARDWARE>, HardwareView>
        implements Hardware<COMMAND, COMMAND, COMMAND, VALUE, VALUE, PROPERTY, VALUE, COMMANDS, VALUES, PROPERTIES, DEVICES, HARDWARE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<COMMANDS> commandsFactory;
    private final ProxyObject.Factory<VALUE> valueFactory;
    private final ProxyObject.Factory<VALUES> valuesFactory;
    private final ProxyObject.Factory<PROPERTY> propertyFactory;
    private final ProxyObject.Factory<PROPERTIES> propertiesFactory;
    private final ProxyObject.Factory<DEVICES> devicesFactory;

    private COMMAND renameCommand;
    private COMMAND removeCommand;
    private VALUE runningValue;
    private COMMAND startCommand;
    private COMMAND stopCommand;
    private VALUE errorValue;
    private PROPERTY driverProperty;
    private VALUE driverLoadedValue;
    private COMMANDS commands;
    private VALUES values;
    private PROPERTIES properties;
    private DEVICES devices;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyHardware(Logger logger,
                         String name,
                         ManagedCollectionFactory managedCollectionFactory,
                         Receiver.Factory receiverFactory,
                         ProxyObject.Factory<COMMAND> commandFactory,
                         ProxyObject.Factory<COMMANDS> commandsFactory,
                         ProxyObject.Factory<VALUE> valueFactory,
                         ProxyObject.Factory<VALUES> valuesFactory,
                         ProxyObject.Factory<PROPERTY> propertyFactory,
                         ProxyObject.Factory<PROPERTIES> propertiesFactory,
                         ProxyObject.Factory<DEVICES> devicesFactory) {
        super(logger, name, Hardware.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.commandsFactory = commandsFactory;
        this.valueFactory = valueFactory;
        this.valuesFactory = valuesFactory;
        this.propertyFactory = propertyFactory;
        this.propertiesFactory = propertiesFactory;
        this.devicesFactory = devicesFactory;
    }

    @Override
    public HardwareView createView(View.Mode mode) {
        return new HardwareView(mode);
    }

    @Override
    public Tree getTree(HardwareView view, ValueBase.Listener listener) {

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(new ValueView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(START_ID, startCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(new PropertyView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(new ValueView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(DEVICES_ID, devices.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValue(), listener));
                    result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommand(), listener));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommand(), listener));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), listener));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), listener));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), listener));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), listener));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), listener));
                    result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener));
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener));
                    if(view.getRunningValue() != null)
                        result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValue(), listener));
                    if(view.getStartCommand() != null)
                        result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommand(), listener));
                    if(view.getStopCommand() != null)
                        result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommand(), listener));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener));
                    if(view.getDriverProperty() != null)
                        result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), listener));
                    if(view.getDriverLoadedValue() != null)
                        result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), listener));
                    if(view.getCommands() != null)
                        result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), listener));
                    if(view.getValues() != null)
                        result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), listener));
                    if(view.getProperties() != null)
                        result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), listener));
                    if(view.getDevices() != null)
                        result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(HardwareView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(startCommand == null)
                    startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
                if(stopCommand == null)
                    stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
                if(runningValue == null)
                    runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
                if(errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(commands == null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                if(properties == null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(devices == null)
                    devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID), ChildUtil.name(name, DEVICES_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommand() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommand() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(startCommand == null && view.getStartCommand() != null)
                    startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
                if(stopCommand == null && view.getStopCommand() != null)
                    stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
                if(runningValue == null && view.getRunningValue() != null)
                    runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
                if(errorValue == null && view.getErrorValue() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null && view.getDriverProperty() != null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null && view.getDriverLoadedValue() != null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(commands == null && view.getCommands() != null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null && view.getValues() != null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                if(properties == null && view.getProperties() != null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(devices == null && view.getDevices() != null)
                    devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID), ChildUtil.name(name, DEVICES_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                removeCommand.load(new CommandView(View.Mode.ANCESTORS));
                startCommand.load(new CommandView(View.Mode.ANCESTORS));
                stopCommand.load(new CommandView(View.Mode.ANCESTORS));
                runningValue.load(new ValueView(View.Mode.ANCESTORS));
                errorValue.load(new ValueView(View.Mode.ANCESTORS));
                driverProperty.load(new PropertyView(View.Mode.ANCESTORS));
                driverLoadedValue.load(new ValueView(View.Mode.ANCESTORS));
                commands.load(new ListView(View.Mode.ANCESTORS));
                values.load(new ListView(View.Mode.ANCESTORS));
                properties.load(new ListView(View.Mode.ANCESTORS));
                devices.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommand() != null)
                    renameCommand.load(view.getRenameCommand());
                if(view.getRemoveCommand() != null)
                    removeCommand.load(view.getRemoveCommand());
                if(view.getStartCommand() != null)
                    startCommand.load(view.getStartCommand());
                if(view.getStopCommand() != null)
                    stopCommand.load(view.getStopCommand());
                if(view.getRunningValue() != null)
                    runningValue.load(view.getRunningValue());
                if(view.getErrorValue() != null)
                    errorValue.load(view.getErrorValue());
                if(view.getDriverProperty() != null)
                    driverProperty.load(view.getDriverProperty());
                if(view.getDriverLoadedValue() != null)
                    driverLoadedValue.load(view.getDriverLoadedValue());
                if(view.getCommands() != null)
                    commands.load(view.getCommands());
                if(view.getValues() != null)
                    values.load(view.getValues());
                if(view.getProperties() != null)
                    properties.load(view.getProperties());
                if(view.getDevices() != null)
                    devices.load(view.getDevices());
                break;
        }
    }

    @Override
    public void loadRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.load(commandView);
    }

    @Override
    public void loadRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.load(commandView);
    }

    @Override
    public void loadStartCommand(CommandView commandView) {
        if(startCommand == null)
            startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
        startCommand.load(commandView);
    }

    @Override
    public void loadStopCommand(CommandView commandView) {
        if(stopCommand == null)
            stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
        stopCommand.load(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(removeCommand != null)
            removeCommand.uninit();
        if(runningValue != null)
            runningValue.uninit();
        if(startCommand != null)
            startCommand.uninit();
        if(stopCommand != null)
            stopCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        if(driverProperty != null)
            driverProperty.uninit();
        if(driverLoadedValue != null)
            driverLoadedValue.uninit();
        if(commands != null)
            commands.uninit();
        if(values != null)
            values.uninit();
        if(properties != null)
            properties.uninit();
        if(devices != null)
            devices.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final boolean isRunning() {
        return runningValue.getValue() != null
                && runningValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(runningValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getRunningValue() {
        return runningValue;
    }

    @Override
    public COMMAND getStartCommand() {
        return startCommand;
    }

    @Override
    public COMMAND getStopCommand() {
        return stopCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null
                && driverLoadedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public COMMANDS getCommands() {
        return commands;
    }

    @Override
    public VALUES getValues() {
        return values;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    public final DEVICES getDeviceConnecteds() {
        return devices;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        } else if(RUNNING_ID.equals(id)) {
            if(runningValue == null)
                runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
            return runningValue;
        } else if(START_ID.equals(id)) {
            if(startCommand == null)
                startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
            return startCommand;
        } else if(STOP_ID.equals(id)) {
            if(stopCommand == null)
                stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
            return stopCommand;
        } else if(ERROR_ID.equals(id)) {
            if(errorValue == null)
                errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
            return errorValue;
        } else if(DRIVER_ID.equals(id)) {
            if(driverProperty == null)
                driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
            return driverProperty;
        } else if(DRIVER_LOADED_ID.equals(id)) {
            if(driverLoadedValue == null)
                driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
            return driverLoadedValue;
        } else if(COMMANDS_ID.equals(id)) {
            if(commands == null)
                commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
            return commands;
        } else if(PROPERTIES_ID.equals(id)) {
            if(properties == null)
                properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
            return properties;
        } else if(VALUES_ID.equals(id)) {
            if(values == null)
                values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.name(name, VALUES_ID));
            return values;
        } else if(DEVICES_ID.equals(id)) {
            if (devices == null)
                devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID), ChildUtil.name(name, DEVICES_ID));
            return devices;
        }
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyHardware<
            ProxyCommand.Simple,
            ProxyList.Simple<ProxyCommand.Simple>,
            ProxyValue.Simple,
            ProxyList.Simple<ProxyValue.Simple>,
            ProxyProperty.Simple,
            ProxyList.Simple<ProxyProperty.Simple>,
            ProxyList.Simple<ProxyDeviceConnected.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory,
                      Factory<ProxyProperty.Simple> propertyFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory,
                      Factory<ProxyList.Simple<ProxyDeviceConnected.Simple>> devicesFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valueFactory, valuesFactory, propertyFactory, propertiesFactory, devicesFactory);
        }
    }
}
