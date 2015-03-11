package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class UserList extends MainList<UserData, GWTProxyUser> {

    public UserList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getUsers(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyUser object) {
        if(object != null)
            return new User(object);
        else
            return new User(childOverview);
    }
}
