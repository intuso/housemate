package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.user.rule.User;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import com.intuso.housemate.web.client.place.UserPlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class UserView extends ObjectListView<GWTProxyUser, UserPlace> implements com.intuso.housemate.web.client.ui.view.UserView {

    public UserView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    protected GWTProxyList<UserWrappable, GWTProxyUser> getList(UserPlace place) {
        return resources.getRoot().getUsers();
    }

    @Override
    protected GWTProxyCommand getAddCommand(UserPlace place) {
        return resources.getRoot().getAddUserCommand();
    }

    @Override
    protected String getSelectedObjectName(UserPlace place) {
        return place.getUsername();
    }

    @Override
    protected Widget getObjectWidget(UserPlace place, GWTProxyUser user) {
        return new User(user);
    }

    @Override
    protected UserPlace getPlace(UserPlace place, GWTProxyUser user) {
        if(user == null)
            return new UserPlace();
        else
            return new UserPlace(user.getId());
    }
}