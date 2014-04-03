package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.application.instance.ApplicationInstanceList;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.object.GWTProxyApplication;

/**
 */
public class Application extends ConfigurableObject {

    interface ApplicationUiBinder extends UiBinder<Widget, Application> {}

    private static ApplicationUiBinder ourUiBinder = GWT.create(ApplicationUiBinder.class);

    @UiField(provided = true)
    ApplicationInstanceList instanceList;

    private final GWTProxyApplication application;

    public Application(final GWTProxyApplication application) {

        this.application = application;

        instanceList = new ApplicationInstanceList(application.getApplicationInstances(), "instances", null, true);

        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new ApplicationSettings(application);
    }
}
