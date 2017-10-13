package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceConnectedView extends DeviceView<DeviceConnectedView> {

    public DeviceConnectedView() {}

    public DeviceConnectedView(Mode mode) {
        super(mode);
    }

    public DeviceConnectedView(CommandView renameCommandView, ListView<CommandView> commandsView, ListView<ValueView> valuesView) {
        super(renameCommandView, commandsView, valuesView);
    }
}
