package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceGroupView extends DeviceView<DeviceGroupView> {

    private ValueView errorValue;
    private CommandView removeCommand;

    public DeviceGroupView() {}

    public DeviceGroupView(Mode mode) {
        super(mode);
    }

    public DeviceGroupView(CommandView renameCommandView,
                           ValueView errorValue,
                           CommandView removeCommand,
                           ListView<CommandView> commandsView,
                           ListView<ValueView> valuesView) {
        super(renameCommandView, commandsView, valuesView);
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public DeviceGroupView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public DeviceGroupView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }
}
