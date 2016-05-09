package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.object.GeneralOptions;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 */
public class UserSettings extends Composite {

    interface UserUiBinder extends UiBinder<Widget, UserSettings> {}

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    @UiField(provided = true)
    GeneralOptions generalOptions;

    public UserSettings(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyUser user) {
        generalOptions = new GeneralOptions(types, user);
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
