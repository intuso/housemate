package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceConnectedView extends View<DeviceConnectedView> {

    private CommandView renameCommandView;
    private ListView<CommandView> commandsView;
    private ListView<ValueView> valuesView;

    public DeviceConnectedView() {}

    public DeviceConnectedView(Mode mode) {
        super(mode);
    }

    public DeviceConnectedView(CommandView renameCommandView, ListView<CommandView> commandsView, ListView<ValueView> valuesView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.commandsView = commandsView;
        this.valuesView = valuesView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public DeviceConnectedView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public ListView<CommandView> getCommandsView() {
        return commandsView;
    }

    public DeviceConnectedView setCommandsView(ListView<CommandView> commandsView) {
        this.commandsView = commandsView;
        return this;
    }

    public ListView<ValueView> getValuesView() {
        return valuesView;
    }

    public DeviceConnectedView setValuesView(ListView<ValueView> valuesView) {
        this.valuesView = valuesView;
        return this;
    }
}
