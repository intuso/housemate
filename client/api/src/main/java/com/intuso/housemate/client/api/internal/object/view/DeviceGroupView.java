package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceGroupView extends DeviceView<DeviceGroupView> {

    private ValueView errorValueView;
    private CommandView removeCommandView;

    public DeviceGroupView() {}

    public DeviceGroupView(Mode mode) {
        super(mode);
    }

    public DeviceGroupView(CommandView renameCommandView,
                           ValueView errorValueView,
                           CommandView removeCommandView,
                           ListView<CommandView> commandsView,
                           ListView<ValueView> valuesView) {
        super(renameCommandView, commandsView, valuesView);
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
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
}
