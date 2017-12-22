package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class HardwareView extends View {

    private CommandView renameCommand;
    private CommandView startCommand;
    private CommandView stopCommand;
    private ValueView runningValue;
    private ValueView errorValue;
    private CommandView removeCommand;
    private PropertyView driverProperty;
    private ValueView driverLoadedValue;
    private ValueView satisfiedValue;
    private ListView<CommandView> commands;
    private ListView<ValueView> values;
    private ListView<PropertyView> properties;
    private ListView<DeviceConnectedView> devices;

    public HardwareView() {}

    public HardwareView(Mode mode) {
        super(mode);
    }

    public HardwareView(CommandView renameCommand,
                        CommandView startCommand,
                        CommandView stopCommand,
                        ValueView runningValue,
                        ValueView errorValue,
                        CommandView removeCommand,
                        PropertyView driverProperty,
                        ValueView driverLoadedValue,
                        ValueView satisfiedValue,
                        ListView<CommandView> commands,
                        ListView<ValueView> values,
                        ListView<PropertyView> properties,
                        ListView<DeviceConnectedView> devices) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.startCommand = startCommand;
        this.stopCommand = stopCommand;
        this.runningValue = runningValue;
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
        this.driverProperty = driverProperty;
        this.driverLoadedValue = driverLoadedValue;
        this.satisfiedValue = satisfiedValue;
        this.commands = commands;
        this.values = values;
        this.properties = properties;
        this.devices = devices;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public HardwareView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public CommandView getStartCommand() {
        return startCommand;
    }

    public HardwareView setStartCommand(CommandView startCommand) {
        this.startCommand = startCommand;
        return this;
    }

    public CommandView getStopCommand() {
        return stopCommand;
    }

    public HardwareView setStopCommand(CommandView stopCommand) {
        this.stopCommand = stopCommand;
        return this;
    }

    public ValueView getRunningValue() {
        return runningValue;
    }

    public HardwareView setRunningValue(ValueView runningValue) {
        this.runningValue = runningValue;
        return this;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public HardwareView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public HardwareView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public PropertyView getDriverProperty() {
        return driverProperty;
    }

    public HardwareView setDriverProperty(PropertyView driverProperty) {
        this.driverProperty = driverProperty;
        return this;
    }

    public ValueView getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public HardwareView setDriverLoadedValue(ValueView driverLoadedValue) {
        this.driverLoadedValue = driverLoadedValue;
        return this;
    }

    public ValueView getSatisfiedValue() {
        return satisfiedValue;
    }

    public HardwareView setSatisfiedValue(ValueView satisfiedValue) {
        this.satisfiedValue = satisfiedValue;
        return this;
    }

    public ListView<CommandView> getCommands() {
        return commands;
    }

    public HardwareView setCommands(ListView<CommandView> commands) {
        this.commands = commands;
        return this;
    }

    public ListView<ValueView> getValues() {
        return values;
    }

    public HardwareView setValues(ListView<ValueView> values) {
        this.values = values;
        return this;
    }

    public ListView<PropertyView> getProperties() {
        return properties;
    }

    public HardwareView setProperties(ListView<PropertyView> properties) {
        this.properties = properties;
        return this;
    }

    public ListView<DeviceConnectedView> getDevices() {
        return devices;
    }

    public HardwareView setDevices(ListView<DeviceConnectedView> devices) {
        this.devices = devices;
        return this;
    }
}
