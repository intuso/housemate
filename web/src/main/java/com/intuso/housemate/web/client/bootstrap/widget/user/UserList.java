package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyUser;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class UserList extends MainList<com.intuso.housemate.client.v1_0.api.object.User.Data, GWTProxyUser> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.User.Data, GWTProxyUser> users;

    public UserList(String title, GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyList<com.intuso.housemate.client.v1_0.api.object.User.Data, GWTProxyUser> users, GWTProxyCommand addCommand) {
        super(title, types, addCommand);
        this.types = types;
        this.users = users;
        setList(users);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new User(types, users, childOverview));
    }
}
