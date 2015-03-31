package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
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
public class UserList extends MainList<UserData, GWTProxyUser> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final GWTProxyList<UserData, GWTProxyUser> users;

    public UserList(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<UserData, GWTProxyUser> users, String name) {
        super(name, null, true);
        this.types = types;
        this.users = users;
        setList(users);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new User(types, users, childOverview));
    }
}
