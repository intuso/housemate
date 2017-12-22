package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class TaskView extends View {

    private CommandView renameCommand;
    private ValueView errorValue;
    private CommandView removeCommand;
    private PropertyView driverProperty;
    private ValueView driverLoadedValue;
    private ValueView executingValue;
    private ListView<PropertyView> properties;

    public TaskView() {}

    public TaskView(Mode mode) {
        super(mode);
    }

    public TaskView(CommandView renameCommand,
                    ValueView errorValue,
                    CommandView removeCommand,
                    PropertyView driverProperty,
                    ValueView driverLoadedValue,
                    ValueView executingValue,
                    ListView<PropertyView> properties) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
        this.driverProperty = driverProperty;
        this.driverLoadedValue = driverLoadedValue;
        this.executingValue = executingValue;
        this.properties = properties;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public TaskView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public TaskView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public TaskView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public PropertyView getDriverProperty() {
        return driverProperty;
    }

    public TaskView setDriverProperty(PropertyView driverProperty) {
        this.driverProperty = driverProperty;
        return this;
    }

    public ValueView getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public TaskView setDriverLoadedValue(ValueView driverLoadedValue) {
        this.driverLoadedValue = driverLoadedValue;
        return this;
    }

    public ValueView getExecutingValue() {
        return executingValue;
    }

    public TaskView setExecutingValue(ValueView executingValue) {
        this.executingValue = executingValue;
        return this;
    }

    public ListView<PropertyView> getProperties() {
        return properties;
    }

    public TaskView setProperties(ListView<PropertyView> properties) {
        this.properties = properties;
        return this;
    }
}
