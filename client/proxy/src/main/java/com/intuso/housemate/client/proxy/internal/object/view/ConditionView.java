package com.intuso.housemate.client.proxy.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ConditionView extends View<ConditionView> {

    private CommandView renameCommandView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private PropertyView driverPropertyView;
    private ValueView driverLoadedValueView;
    private ValueView satisfiedValueView;
    private ListView<PropertyView> propertiesView;
    private ListView<ConditionView> conditionsView;
    private CommandView addConditionCommandView;

    public ConditionView() {}

    public ConditionView(Mode mode) {
        super(mode);
    }

    public ConditionView(CommandView renameCommandView,
                         ValueView errorValueView,
                         CommandView removeCommandView,
                         PropertyView driverPropertyView,
                         ValueView driverLoadedValueView,
                         ValueView satisfiedValueView,
                         ListView<PropertyView> propertiesView,
                         ListView<ConditionView> conditionsView,
                         CommandView addConditionCommandView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.driverPropertyView = driverPropertyView;
        this.driverLoadedValueView = driverLoadedValueView;
        this.satisfiedValueView = satisfiedValueView;
        this.propertiesView = propertiesView;
        this.conditionsView = conditionsView;
        this.addConditionCommandView = addConditionCommandView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public ConditionView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public ValueView getErrorValueView() {
        return errorValueView;
    }

    public ConditionView setErrorValueView(ValueView errorValueView) {
        this.errorValueView = errorValueView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public ConditionView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public PropertyView getDriverPropertyView() {
        return driverPropertyView;
    }

    public ConditionView setDriverPropertyView(PropertyView driverPropertyView) {
        this.driverPropertyView = driverPropertyView;
        return this;
    }

    public ValueView getDriverLoadedValueView() {
        return driverLoadedValueView;
    }

    public ConditionView setDriverLoadedValueView(ValueView driverLoadedValueView) {
        this.driverLoadedValueView = driverLoadedValueView;
        return this;
    }

    public ValueView getSatisfiedValueView() {
        return satisfiedValueView;
    }

    public ConditionView setSatisfiedValueView(ValueView satisfiedValueView) {
        this.satisfiedValueView = satisfiedValueView;
        return this;
    }

    public ListView<PropertyView> getPropertiesView() {
        return propertiesView;
    }

    public ConditionView setPropertiesView(ListView<PropertyView> propertiesView) {
        this.propertiesView = propertiesView;
        return this;
    }

    public ListView<ConditionView> getConditionsView() {
        return conditionsView;
    }

    public ConditionView setConditionsView(ListView<ConditionView> conditionsView) {
        this.conditionsView = conditionsView;
        return this;
    }

    public CommandView getAddConditionCommandView() {
        return addConditionCommandView;
    }

    public ConditionView setAddConditionCommandView(CommandView addConditionCommandView) {
        this.addConditionCommandView = addConditionCommandView;
        return this;
    }
}
