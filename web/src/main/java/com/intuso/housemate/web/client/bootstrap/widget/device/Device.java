package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandList;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.bootstrap.widget.value.ValueList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

/**
 */
public class Device extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, Device> {
    }

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField
    public PerformButton startButton;
    @UiField
    public PerformButton stopButton;
    @UiField
    public PerformButton removeButton;
    @UiField
    public CommandList commandList;
    @UiField
    public ValueList valueList;
    @UiField
    public PropertyList propertyList;

    public Device(GWTProxyDevice device) {

        initWidget(ourUiBinder.createAndBindUi(this));

        startButton.setCommand(device.getStartCommand());
        stopButton.setCommand(device.getStopCommand());
        removeButton.setCommand(device.getRemoveCommand());
        commandList.setList(device.getCommands());
        valueList.setList(device.getValues());
        propertyList.setList(device.getProperties());
    }
}
