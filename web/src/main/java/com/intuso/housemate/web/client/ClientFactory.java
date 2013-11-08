package com.intuso.housemate.web.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.housemate.web.client.ui.view.*;
import com.intuso.housemate.web.client.ui.view.UsersView;

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

    // housemate resources
    GWTProxyFeatureFactory getFeatureFactory();

    // UI things
    LoginView getLoginView();
    Page getPage();
    UsersView getUsersView();
    DevicesView getDevicesView();
    AutomationsView getAutomationsView();
}
