package com.intuso.housemate.web.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.housemate.web.client.ui.view.AccountView;
import com.intuso.housemate.web.client.ui.view.AutomationView;
import com.intuso.housemate.web.client.ui.view.ConditionView;
import com.intuso.housemate.web.client.ui.view.DeviceView;
import com.intuso.housemate.web.client.ui.view.HomeView;
import com.intuso.housemate.web.client.ui.view.LoginView;
import com.intuso.housemate.web.client.ui.view.Page;
import com.intuso.housemate.web.client.ui.view.SatisfiedTaskView;
import com.intuso.housemate.web.client.ui.view.UnsatisfiedTaskView;
import com.intuso.housemate.web.client.ui.view.UserView;

/**
 */
public interface ClientFactory {

    // general things
    EventBus getEventBus();

    // activity/place stuff
    HousemateActivityMapper getActivityMapper();
    HousematePlaceHistoryMapper getPlaceHistoryMapper();
    PlaceHistoryHandler getPlaceHistoryHandler();
    PlaceController getPlaceController();

    // UI things
    LoginView getLoginView();
    Page getPage();
    HomeView getHomeView();
    UserView getUserView();
    DeviceView getDeviceView();
    AutomationView getAutomationView();
    ConditionView getConditionView();
    SatisfiedTaskView getSatisfiedTaskView();
    UnsatisfiedTaskView getUnsatisfiedTaskView();
    AccountView getAccountView();
}
