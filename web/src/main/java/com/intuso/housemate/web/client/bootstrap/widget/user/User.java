package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 */
public class User extends ConfigurableObject {

    interface UserUiBinder extends UiBinder<Widget, User> {}

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    private final GWTProxyUser user;

    public User(final GWTProxyUser user) {
        this.user = user;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new UserSettings(user);
    }
}
