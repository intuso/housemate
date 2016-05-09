package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.object.GeneralOptions;
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
    GeneralOptions generalOptions;

    private final GWTProxyAutomation automation;

    public AutomationSettings(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyAutomation automation) {
        this.automation = automation;

        generalOptions = new GeneralOptions(types, automation);

        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
