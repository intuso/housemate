package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.object.GWTProxyApplication;

/**
 */
public class ApplicationSettings extends Composite {

    interface ApplicationSettingsUiBinder extends UiBinder<Widget, ApplicationSettings> {}

    private static ApplicationSettingsUiBinder ourUiBinder = GWT.create(ApplicationSettingsUiBinder.class);

    @UiField(provided = true)
    Control control;

    public ApplicationSettings(final GWTProxyApplication application) {
        control = new Control(application);
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
