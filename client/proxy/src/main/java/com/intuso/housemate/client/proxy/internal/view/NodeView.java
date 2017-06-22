package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class NodeView extends View<NodeView> {

    private ListView<TypeView> typesView;
    private ListView<HardwareView> hardwaresView;
    private CommandView addHardwareCommandView;

    public NodeView() {}

    public NodeView(Mode mode) {
        super(mode);
    }

    public NodeView(ListView<TypeView> typesView, ListView<HardwareView> hardwaresView, CommandView addHardwareCommandView) {
        super(Mode.SELECTION);
        this.typesView = typesView;
        this.hardwaresView = hardwaresView;
        this.addHardwareCommandView = addHardwareCommandView;
    }

    public ListView<TypeView> getTypesView() {
        return typesView;
    }

    public NodeView setTypesView(ListView<TypeView> typesView) {
        this.typesView = typesView;
        return this;
    }

    public ListView<HardwareView> getHardwaresView() {
        return hardwaresView;
    }

    public NodeView setHardwaresView(ListView<HardwareView> hardwaresView) {
        this.hardwaresView = hardwaresView;
        return this;
    }

    public CommandView getAddHardwareCommandView() {
        return addHardwareCommandView;
    }

    public NodeView setAddHardwareCommandView(CommandView addHardwareCommandView) {
        this.addHardwareCommandView = addHardwareCommandView;
        return this;
    }
}
