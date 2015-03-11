package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 */
public class UserBody extends Composite {

    interface UserUiBinder extends UiBinder<Widget, UserBody> {}

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    public UserBody(final GWTProxyUser user) {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
