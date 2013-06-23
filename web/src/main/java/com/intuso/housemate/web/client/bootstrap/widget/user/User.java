package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class User extends Composite {

    interface UserUiBinder extends UiBinder<Widget, User> {
    }

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    @UiField
    public PerformButton removeButton;

    public User(final GWTProxyUser user) {

        initWidget(ourUiBinder.createAndBindUi(this));

        removeButton.setCommand(user.getRemoveCommand());
    }
}
