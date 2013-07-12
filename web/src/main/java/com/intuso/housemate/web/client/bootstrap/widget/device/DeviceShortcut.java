package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.proxy.ChildLoadedListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

/**
 */
public class DeviceShortcut extends Composite {

    interface DeviceShortcutUiBinder extends UiBinder<Widget, DeviceShortcut> {
    }

    private static DeviceShortcutUiBinder ourUiBinder = GWT.create(DeviceShortcutUiBinder.class);

    @UiField
    public Heading deviceName;
    @UiField
    public ButtonGroup commandButtonGroup;

    public DeviceShortcut(final GWTProxyDevice device) {

        initWidget(ourUiBinder.createAndBindUi(this));

        deviceName.setText(device.getName());

        device.addChildLoadedListener(com.intuso.housemate.api.object.device.Device.COMMANDS_ID, new ChildLoadedListener<GWTProxyDevice, ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(GWTProxyDevice object, ProxyObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
                device.getCommands().addObjectListener(new ListListener<GWTProxyCommand>() {
                    @Override
                    public void elementAdded(GWTProxyCommand command) {
                        commandButtonGroup.add(new PerformButton(command, command.getId()));
                    }

                    @Override
                    public void elementRemoved(GWTProxyCommand command) {
                        // remove it
                    }
                }, true);
            }
        });
    }
}
