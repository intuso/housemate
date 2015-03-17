package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyHardware;

/**
 */
public class HardwareSettings extends Composite {

    interface HardwareSettingsUiBinder extends UiBinder<Widget, HardwareSettings> {}

    private static HardwareSettingsUiBinder ourUiBinder = GWT.create(HardwareSettingsUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField
    PropertyList properties;

    public HardwareSettings(final GWTProxyHardware hardware) {
        control = new Control(hardware);
        initWidget(ourUiBinder.createAndBindUi(this));
        properties.setList(hardware.getProperties());
    }
}
