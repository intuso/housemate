package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyHardware;

/**
 */
public class HardwareBody extends Composite {

    interface HardwareUiBinder extends UiBinder<Widget, HardwareBody> {}

    private static HardwareUiBinder ourUiBinder = GWT.create(HardwareUiBinder.class);

    @UiField
    PropertyList properties;

    public HardwareBody(final GWTProxyHardware hardware) {
        initWidget(ourUiBinder.createAndBindUi(this));
        properties.setList(hardware.getProperties());
    }
}
