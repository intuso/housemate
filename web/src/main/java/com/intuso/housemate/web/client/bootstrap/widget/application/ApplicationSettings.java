package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class ApplicationSettings extends Composite {

    interface ApplicationSettingsUiBinder extends UiBinder<Widget, ApplicationSettings> {}

    private static ApplicationSettingsUiBinder ourUiBinder = GWT.create(ApplicationSettingsUiBinder.class);

    @UiField(provided = true)
    Control control;

    public ApplicationSettings(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyApplication application) {
        control = new Control(types, application);
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
