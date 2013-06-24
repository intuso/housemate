package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.ui.view.HomeView;

/**
 */
public class HomeActivity extends HousemateActivity<HomePlace, HomeView> {

    protected HomeActivity(HomePlace place) {
        super(place);
    }

    @Override
    public HomeView getView() {
        return Housemate.FACTORY.getHomeView();
    }
}
