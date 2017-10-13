package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceView<VIEW extends DeviceView<?>> extends View {

    private CommandView renameCommandView;
    private ListView<CommandView> commandsView;
    private ListView<ValueView> valuesView;

    public DeviceView() {}

    public DeviceView(Mode mode) {
        super(mode);
    }

    public DeviceView(CommandView renameCommandView, ListView<CommandView> commandsView, ListView<ValueView> valuesView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.commandsView = commandsView;
        this.valuesView = valuesView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public DeviceView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public ListView<CommandView> getCommandsView() {
        return commandsView;
    }

    public VIEW setCommandsView(ListView<CommandView> commandsView) {
        this.commandsView = commandsView;
        return (VIEW) this;
    }

    public ListView<ValueView> getValuesView() {
        return valuesView;
    }

    public VIEW setValuesView(ListView<ValueView> valuesView) {
        this.valuesView = valuesView;
        return (VIEW) this;
    }
}
