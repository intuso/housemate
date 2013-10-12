package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.UsersPlace;
import com.intuso.housemate.web.client.ui.view.UserView;

/**
 */
public class UserActivity extends HousemateActivity<UsersPlace, UserView> {

    protected UserActivity(UsersPlace place) {
        super(place);
    }

    @Override
    public UserView getView() {
        return Housemate.FACTORY.getUserView();
    }
}
