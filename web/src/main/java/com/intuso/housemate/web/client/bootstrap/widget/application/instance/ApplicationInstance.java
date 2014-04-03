package com.intuso.housemate.web.client.bootstrap.widget.application.instance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.object.GWTProxyApplicationInstance;

/**
 */
public class ApplicationInstance extends Composite {

    interface ApplicationInstanceUiBinder extends UiBinder<Widget, ApplicationInstance> {}

    private static ApplicationInstanceUiBinder ourUiBinder = GWT.create(ApplicationInstanceUiBinder.class);

    @UiField
    AccessWidget accessWidget;

    public ApplicationInstance(final GWTProxyApplicationInstance applicationInstance) {
        initWidget(ourUiBinder.createAndBindUi(this));
        accessWidget.setApplicationInstance(applicationInstance);
    }
}
