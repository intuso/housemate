package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 */
public class User extends ObjectWidget<GWTProxyUser> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public User(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.User.Data, GWTProxyUser> users, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyUser user = users.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            users.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load user");
                }

                @Override
                public void succeeded() {
                    loading(false);
                    setObject(users.get(childOverview.getId()));
                }
            }, new TreeLoadInfo(childOverview.getId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
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
