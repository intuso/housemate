package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class DeviceShortcut extends Composite {

    interface DeviceShortcutUiBinder extends UiBinder<Widget, DeviceShortcut> {
    }

    private static DeviceShortcutUiBinder ourUiBinder = GWT.create(DeviceShortcutUiBinder.class);

    @UiField
    public Heading deviceName;
    @UiField
    public ButtonGroup commandButtonGroup;

    public DeviceShortcut(GWTProxyDevice device) {

        initWidget(ourUiBinder.createAndBindUi(this));

        deviceName.setText(device.getName());
        for(GWTProxyCommand command : device.getCommands())
            commandButtonGroup.add(new PerformButton(command, command.getId()));
    }
}
