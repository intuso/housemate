package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceView<VIEW extends DeviceView<?>> extends View {

    private CommandView renameCommand;
    private ListView<DeviceComponentView> components;

    public DeviceView() {}

    public DeviceView(Mode mode) {
        super(mode);
    }

    public DeviceView(CommandView renameCommand, ListView<DeviceComponentView> components) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.components = components;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public DeviceView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public ListView<DeviceComponentView> getComponents() {
        return components;
    }

    public VIEW setComponents(ListView<DeviceComponentView> components) {
        this.components = components;
        return (VIEW) this;
    }
}
