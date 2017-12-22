package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ConditionView extends View {

    private CommandView renameCommand;
    private ValueView errorValue;
    private CommandView removeCommand;
    private PropertyView driverProperty;
    private ValueView driverLoadedValue;
    private ValueView satisfiedValue;
    private ListView<PropertyView> properties;
    private ListView<ConditionView> conditions;
    private CommandView addConditionCommand;

    public ConditionView() {}

    public ConditionView(Mode mode) {
        super(mode);
    }

    public ConditionView(CommandView renameCommand,
                         ValueView errorValue,
                         CommandView removeCommand,
                         PropertyView driverProperty,
                         ValueView driverLoadedValue,
                         ValueView satisfiedValue,
                         ListView<PropertyView> properties,
                         ListView<ConditionView> conditions,
                         CommandView addConditionCommand) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
        this.driverProperty = driverProperty;
        this.driverLoadedValue = driverLoadedValue;
        this.satisfiedValue = satisfiedValue;
        this.properties = properties;
        this.conditions = conditions;
        this.addConditionCommand = addConditionCommand;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public ConditionView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public ConditionView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public ConditionView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public PropertyView getDriverProperty() {
        return driverProperty;
    }

    public ConditionView setDriverProperty(PropertyView driverProperty) {
        this.driverProperty = driverProperty;
        return this;
    }

    public ValueView getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public ConditionView setDriverLoadedValue(ValueView driverLoadedValue) {
        this.driverLoadedValue = driverLoadedValue;
        return this;
    }

    public ValueView getSatisfiedValue() {
        return satisfiedValue;
    }

    public ConditionView setSatisfiedValue(ValueView satisfiedValue) {
        this.satisfiedValue = satisfiedValue;
        return this;
    }

    public ListView<PropertyView> getProperties() {
        return properties;
    }

    public ConditionView setProperties(ListView<PropertyView> properties) {
        this.properties = properties;
        return this;
    }

    public ListView<ConditionView> getConditions() {
        return conditions;
    }

    public ConditionView setConditions(ListView<ConditionView> conditions) {
        this.conditions = conditions;
        return this;
    }

    public CommandView getAddConditionCommand() {
        return addConditionCommand;
    }

    public ConditionView setAddConditionCommand(CommandView addConditionCommand) {
        this.addConditionCommand = addConditionCommand;
        return this;
    }
}
