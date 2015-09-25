package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.intuso.housemate.object.v1_0.api.List;
import com.intuso.housemate.web.client.bootstrap.widget.user.UserList;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.object.GWTProxyServer;
import com.intuso.housemate.web.client.place.UsersPlace;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class UsersView extends FlowPanel
        implements com.intuso.housemate.web.client.ui.view.UsersView,
            List.Listener<GWTProxyServer>,
            SelectedIdsChangedHandler {

    private final PlaceHistoryMapper placeHistoryMapper;

    @Inject
    public UsersView(PlaceHistoryMapper placeHistoryMapper, GWTProxyRoot root) {
        this.placeHistoryMapper = placeHistoryMapper;
        root.getServers().addObjectListener(this, true);
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(placeHistoryMapper.getToken(new UsersPlace(ids)), false);
    }

    @Override
    public void newPlace(UsersPlace place) {

    }

    @Override
    public void elementAdded(GWTProxyServer client) {
        add(new UserList(client.getName(), client.getTypes(), client.getUsers(), client.getAddUserCommand()));
    }

    @Override
    public void elementRemoved(GWTProxyServer client) {
        // do nothing atm
    }
}
