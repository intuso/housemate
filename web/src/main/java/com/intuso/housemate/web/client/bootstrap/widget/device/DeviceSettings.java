package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.object.GeneralOptions;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class DeviceSettings extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, DeviceSettings> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField(provided = true)
    GeneralOptions generalOptions;
    @UiField
    PropertyList propertyList;

    public DeviceSettings(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyDevice device) {
        generalOptions = new GeneralOptions(types, device);
        initWidget(ourUiBinder.createAndBindUi(this));
        propertyList.setTypes(types);
        propertyList.setList(device.getProperties());
    }
}
