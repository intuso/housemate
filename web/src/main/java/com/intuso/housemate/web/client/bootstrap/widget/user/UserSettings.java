package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 */
public class UserSettings extends Composite {

    interface UserUiBinder extends UiBinder<Widget, UserSettings> {}

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    @UiField(provided = true)
    Control control;

    public UserSettings(final GWTProxyUser user) {
        control = new Control(user);
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
