package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.ui.view.HomeView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
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
