package com.intuso.housemate.web.client.bootstrap.widget.user;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectList;
import com.intuso.housemate.web.client.object.GWTProxyUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class UserList extends ObjectList<UserData, GWTProxyUser> {

    public UserList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.ENVIRONMENT.getResources().getRoot().getUsers(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(final String id, GWTProxyUser object) {
        if(object != null)
            return new User(object);
        else {
            final SimplePanel panel = new SimplePanel();
            Housemate.ENVIRONMENT.getResources().getRoot().getUsers().load(new LoadManager("load-user",
                    new HousemateObject.TreeLoadInfo(id, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))) {
                @Override
                protected void failed(HousemateObject.TreeLoadInfo path) {
                    panel.remove(panel.getWidget());
                    panel.add(new Label("Failed to load user"));
                }

                @Override
                protected void allLoaded() {
                    panel.add(new User(Housemate.ENVIRONMENT.getResources().getRoot().getUsers().get(id)));
                }
            });
            return panel;
        }
    }
}
