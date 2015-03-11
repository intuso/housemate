package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.application.instance.ApplicationInstanceList;
import com.intuso.housemate.web.client.object.GWTProxyApplication;

/**
 */
public class ApplicationBody extends Composite {

    interface ApplicationUiBinder extends UiBinder<Widget, ApplicationBody> {}

    private static ApplicationUiBinder ourUiBinder = GWT.create(ApplicationUiBinder.class);

    @UiField
    ApplicationInstanceList instanceList;

    public ApplicationBody(final GWTProxyApplication application) {
        initWidget(ourUiBinder.createAndBindUi(this));
        instanceList.setList(application.getApplicationInstances());
    }
}
