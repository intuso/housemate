package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.common.collect.Lists;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.user.UserList;
import com.intuso.housemate.web.client.handler.MultiListSelectedIdsChangedHandler;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.place.UsersPlace;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class UsersView extends FlowPanel
        implements com.intuso.housemate.web.client.ui.view.UsersView, SelectedIdsChangedHandler {

    private final PlaceHistoryMapper placeHistoryMapper;
    private final UserList favouritesList;
    private final UserList allList;
    private final MultiListSelectedIdsChangedHandler selectedIdsChangedHandler;

    @Inject
    public UsersView(PlaceHistoryMapper placeHistoryMapper, GWTProxyRoot root) {

        this.placeHistoryMapper = placeHistoryMapper;

        selectedIdsChangedHandler = new MultiListSelectedIdsChangedHandler(this);

        List<String> favourites = Lists.newArrayList();
        favouritesList = new UserList("favourites", favourites, true);
        favouritesList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);
        allList = new UserList(favourites.size() > 0 ? "all" : "", favourites, false);
        allList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);

        add(favouritesList);
        add(allList);
        Button addButton = new PerformButton(root.getAddUserCommand(), IconType.PLUS);
        addButton.setSize(ButtonSize.SMALL);
        add(addButton);
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(placeHistoryMapper.getToken(new UsersPlace(ids)), false);
    }

    @Override
    public void newPlace(UsersPlace place) {
        favouritesList.setSelected(place.getUserIds());
        allList.setSelected(place.getUserIds());
    }
}
