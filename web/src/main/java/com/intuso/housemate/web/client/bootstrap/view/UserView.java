package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.user.UserList;
import com.intuso.housemate.web.client.place.UsersPlace;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class UserView extends FlowPanel implements com.intuso.housemate.web.client.ui.view.UserView {

    private final UserList favouritesList;
    private final UserList allList;

    public UserView() {

        List<String> favourites = Lists.newArrayList();
        favouritesList = new UserList("favourites", favourites, true);
        allList = new UserList("all", favourites, false);

        add(favouritesList);
        add(allList);
        Button addButton = new PerformButton(Housemate.ENVIRONMENT.getResources().getRoot().getAddUserCommand(), IconType.PLUS);
        addButton.setSize(ButtonSize.SMALL);
        add(addButton);
    }

    @Override
    public void newPlace(UsersPlace place) {
        favouritesList.setSelected(place.getUsernames());
        allList.setSelected(place.getUsernames());
    }
}
