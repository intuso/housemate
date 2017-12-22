package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceView<VIEW extends DeviceView<?>> extends View {

    private CommandView renameCommand;
    private ListView<CommandView> commands;
    private ListView<ValueView> values;

    public DeviceView() {}

    public DeviceView(Mode mode) {
        super(mode);
    }

    public DeviceView(CommandView renameCommand, ListView<CommandView> commands, ListView<ValueView> values) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.commands = commands;
        this.values = values;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public DeviceView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public ListView<CommandView> getCommands() {
        return commands;
    }

    public VIEW setCommands(ListView<CommandView> commands) {
        this.commands = commands;
        return (VIEW) this;
    }

    public ListView<ValueView> getValues() {
        return values;
    }

    public VIEW setValues(ListView<ValueView> values) {
        this.values = values;
        return (VIEW) this;
    }
}
