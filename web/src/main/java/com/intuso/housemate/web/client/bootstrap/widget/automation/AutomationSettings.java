package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class AutomationSettings extends Composite {

    interface AutomationUiBinder extends UiBinder<Widget, AutomationSettings> {
    }

    private static AutomationUiBinder ourUiBinder = GWT.create(AutomationUiBinder.class);

    @UiField(provided = true)
    Control control;

    private final GWTProxyAutomation automation;

    public AutomationSettings(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyAutomation automation) {
        this.automation = automation;

        control = new Control(types, automation);

        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
