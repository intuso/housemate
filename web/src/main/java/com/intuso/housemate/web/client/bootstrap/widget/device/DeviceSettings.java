package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.primary.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

/**
 */
public class DeviceSettings extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, DeviceSettings> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField(provided = true)
    PropertyList propertiesList;

    public DeviceSettings(final GWTProxyDevice device) {

        control = new Control(device);
        propertiesList = new PropertyList(device.getProperties(), "Properties", device.getCustomPropertyIds(), false);

        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
