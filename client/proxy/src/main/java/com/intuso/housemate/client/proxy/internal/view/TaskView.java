package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class TaskView extends View<TaskView> {

    private CommandView renameCommandView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private PropertyView driverPropertyView;
    private ValueView driverLoadedView;
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
                    ValueView driverLoadedView,
                    ValueView executingValueView,
                    ListView<PropertyView> propertiesView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.driverPropertyView = driverPropertyView;
        this.driverLoadedView = driverLoadedView;
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

    public ValueView getDriverLoadedView() {
        return driverLoadedView;
    }

    public TaskView setDriverLoadedView(ValueView driverLoadedView) {
        this.driverLoadedView = driverLoadedView;
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
