package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyHardware;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class HardwareSettings extends Composite {

    interface HardwareSettingsUiBinder extends UiBinder<Widget, HardwareSettings> {}

    private static HardwareSettingsUiBinder ourUiBinder = GWT.create(HardwareSettingsUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField
    PropertyList propertyList;

    public HardwareSettings(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyHardware hardware) {
        control = new Control(types, hardware);
        initWidget(ourUiBinder.createAndBindUi(this));
        propertyList.setTypes(types);
        propertyList.setList(hardware.getProperties());
    }
}
