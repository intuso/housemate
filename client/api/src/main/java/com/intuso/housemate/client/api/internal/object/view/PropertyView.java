package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class PropertyView extends ValueBaseView {

    private CommandView setCommand;

    public PropertyView() {}

    public PropertyView(Mode mode) {
        super(mode);
    }

    public PropertyView(CommandView setCommand) {
        super(Mode.SELECTION);
        this.setCommand = setCommand;
    }

    public CommandView getSetCommand() {
        return setCommand;
    }

    public PropertyView setSetCommand(CommandView setCommand) {
        this.setCommand = setCommand;
        return this;
    }
}
