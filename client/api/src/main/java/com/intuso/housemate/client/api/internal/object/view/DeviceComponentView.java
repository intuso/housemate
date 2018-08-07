package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceComponentView extends View {

    private ListView<CommandView> commands;
    private ListView<ValueView> values;

    public DeviceComponentView() {}

    public DeviceComponentView(Mode mode) {
        super(mode);
    }

    public DeviceComponentView(ListView<CommandView> commands,
                               ListView<ValueView> values) {
        super(Mode.SELECTION);
        this.commands = commands;
        this.values = values;
    }

    public ListView<CommandView> getCommands() {
        return commands;
    }

    public DeviceComponentView setCommands(ListView<CommandView> commands) {
        this.commands = commands;
        return this;
    }

    public ListView<ValueView> getValues() {
        return values;
    }

    public DeviceComponentView setValues(ListView<ValueView> values) {
        this.values = values;
        return this;
    }
}
