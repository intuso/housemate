package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class NodeView extends View {

    private ListView<TypeView> types;
    private ListView<HardwareView> hardwares;
    private CommandView addHardwareCommand;

    public NodeView() {}

    public NodeView(Mode mode) {
        super(mode);
    }

    public NodeView(ListView<TypeView> types, ListView<HardwareView> hardwares, CommandView addHardwareCommand) {
        super(Mode.SELECTION);
        this.types = types;
        this.hardwares = hardwares;
        this.addHardwareCommand = addHardwareCommand;
    }

    public ListView<TypeView> getTypes() {
        return types;
    }

    public NodeView setTypes(ListView<TypeView> types) {
        this.types = types;
        return this;
    }

    public ListView<HardwareView> getHardwares() {
        return hardwares;
    }

    public NodeView setHardwares(ListView<HardwareView> hardwares) {
        this.hardwares = hardwares;
        return this;
    }

    public CommandView getAddHardwareCommand() {
        return addHardwareCommand;
    }

    public NodeView setAddHardwareCommand(CommandView addHardwareCommand) {
        this.addHardwareCommand = addHardwareCommand;
        return this;
    }
}
