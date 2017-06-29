package com.intuso.housemate.client.proxy.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class TaskView extends View<TaskView> {

    private CommandView renameCommandView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private PropertyView driverPropertyView;
    private ValueView driverLoadedValueView;
    private ValueView executingValueView;
    private ListView<PropertyView> propertiesView;

    public TaskView() {}

    public TaskView(Mode mode) {
        super(mode);
    }

    public TaskView(CommandView renameCommandView,
                    ValueView errorValueView,
                    CommandView removeCommandView,
                    PropertyView driverPropertyView,
                    ValueView driverLoadedValueView,
                    ValueView executingValueView,
                    ListView<PropertyView> propertiesView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.driverPropertyView = driverPropertyView;
        this.driverLoadedValueView = driverLoadedValueView;
        this.executingValueView = executingValueView;
        this.propertiesView = propertiesView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public TaskView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public ValueView getErrorValueView() {
        return errorValueView;
    }

    public TaskView setErrorValueView(ValueView errorValueView) {
        this.errorValueView = errorValueView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public TaskView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public PropertyView getDriverPropertyView() {
        return driverPropertyView;
    }

    public TaskView setDriverPropertyView(PropertyView driverPropertyView) {
        this.driverPropertyView = driverPropertyView;
        return this;
    }

    public ValueView getDriverLoadedValueView() {
        return driverLoadedValueView;
    }

    public TaskView setDriverLoadedValueView(ValueView driverLoadedValueView) {
        this.driverLoadedValueView = driverLoadedValueView;
        return this;
    }

    public ValueView getExecutingValueView() {
        return executingValueView;
    }

    public TaskView setExecutingValueView(ValueView executingValueView) {
        this.executingValueView = executingValueView;
        return this;
    }

    public ListView<PropertyView> getPropertiesView() {
        return propertiesView;
    }

    public TaskView setPropertiesView(ListView<PropertyView> propertiesView) {
        this.propertiesView = propertiesView;
        return this;
    }
}
