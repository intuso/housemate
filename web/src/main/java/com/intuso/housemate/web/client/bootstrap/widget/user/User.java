package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 */
public class User extends ObjectWidget<GWTProxyUser> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;

    public User(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyList<UserData, GWTProxyUser> users, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyUser user = users.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            users.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load user");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(users.get(childOverview.getId()));
                }
            }, new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    protected Widget getBodyWidget(GWTProxyUser object) {
        return new UserBody(object);
    }

    @Override
    protected Widget getSettingsWidget(GWTProxyUser object) {
        return new UserSettings(types, object);
    }
}
