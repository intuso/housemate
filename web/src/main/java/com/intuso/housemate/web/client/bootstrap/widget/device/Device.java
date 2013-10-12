package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandList;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.bootstrap.widget.value.ValueList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

/**
 */
public class Device extends ConfigurableObject {

    interface DeviceUiBinder extends UiBinder<Widget, Device> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField
    FlowPanel featureWidgets;

    @UiField(provided = true)
    CommandList commandsList;
    @UiField(provided = true)
    ValueList valuesList;

    final GWTProxyDevice device;

    public Device(final GWTProxyDevice device) {

        this.device = device;

        commandsList = new CommandList(device.getCommands(), "Commands", device.getCustomCommandIds(), false);
        valuesList = new ValueList(device.getValues(), "Values", device.getCustomValueIds(), false);

        initWidget(ourUiBinder.createAndBindUi(this));

        for(String featureId : device.getFeatureIds()) {
            GWTProxyFeature feature = device.getFeature(featureId);
            if(feature != null)
                featureWidgets.add(new WidgetRow(feature.getTitle(), feature.getWidget()));
        }
    }

    @Override
    protected Widget createSettingsWidget() {
        return new DeviceSettings(device);
    }
}
