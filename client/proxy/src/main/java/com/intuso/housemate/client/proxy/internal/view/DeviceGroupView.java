package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceGroupView extends View<DeviceGroupView> {

    private CommandView renameCommandView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private ListView<CommandView> commandsView;
    private ListView<ValueView> valuesView;

    public DeviceGroupView() {}

    public DeviceGroupView(Mode mode) {
        super(mode);
    }

    public DeviceGroupView(CommandView renameCommandView,
                           ValueView errorValueView,
                           CommandView removeCommandView,
                           ListView<CommandView> commandsView,
                           ListView<ValueView> valuesView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.commandsView = commandsView;
        this.valuesView = valuesView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public DeviceGroupView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public ValueView getErrorValueView() {
        return errorValueView;
    }

    public DeviceGroupView setErrorValueView(ValueView errorValueView) {
        this.errorValueView = errorValueView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public DeviceGroupView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public ListView<CommandView> getCommandsView() {
        return commandsView;
    }

    public DeviceGroupView setCommandsView(ListView<CommandView> commandsView) {
        this.commandsView = commandsView;
        return this;
    }

    public ListView<ValueView> getValuesView() {
        return valuesView;
    }

    public DeviceGroupView setValuesView(ListView<ValueView> valuesView) {
        this.valuesView = valuesView;
        return this;
    }
}
