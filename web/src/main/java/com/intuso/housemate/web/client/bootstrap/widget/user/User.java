package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 */
public class User extends Composite {

    interface UserUiBinder extends UiBinder<Widget, User> {
    }

    private static UserUiBinder ourUiBinder = GWT.create(UserUiBinder.class);

    @UiField
    Button settings;
    @UiField
    Collapse settingsPanel;
    @UiField(provided = true)
    Control control;

    public User(final GWTProxyUser user) {
        control = new Control(user);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("settings")
    public void settingsClicked(ClickEvent event) {
        settings.setActive(!settings.isActive());
        if(settings.isActive())
            settingsPanel.show();
        else
            settingsPanel.hide();
    }
}
