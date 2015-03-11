package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 */
public class User extends ObjectWidget<GWTProxyUser> {

    public User(final ChildOverview childOverview) {
        setName(childOverview.getName());
        Housemate.INJECTOR.getProxyRoot().getUsers().load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(HousemateObject.TreeLoadInfo path) {
                setMessage(AlertType.WARNING, "Failed to load user");
            }

            @Override
            public void allLoaded() {
                setObject(Housemate.INJECTOR.getProxyRoot().getUsers().get(childOverview.getId()));
            }
        }, "loadUser-" + childOverview.getId(),
                new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
    }

    public User(final GWTProxyUser user) {
        setObject(user);
    }

    @Override
    protected Widget getBodyWidget(GWTProxyUser object) {
        return new UserBody(object);
    }

    @Override
    protected Widget getSettingsWidget(GWTProxyUser object) {
        return new UserSettings(object);
    }
}
