package com.intuso.housemate.web.client.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.intuso.housemate.web.client.ClientFactory;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.bootstrap.view.Page;
import com.intuso.housemate.web.client.bootstrap.widget.login.LoginPopup;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.housemate.web.client.ui.view.AutomationsView;
import com.intuso.housemate.web.client.ui.view.DevicesView;
import com.intuso.housemate.web.client.ui.view.LoginView;
import com.intuso.housemate.web.client.ui.view.UsersView;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public class BootstrapClientFactory implements ClientFactory {

    // general things
    private final EventBus eventBus = new SimpleEventBus();

    // activity/place stuff
    private final HousemateActivityMapper activityMapper = new HousemateActivityMapper();
    private final HousematePlaceHistoryMapper placeHistoryMapper = GWT.create(HousematePlaceHistoryMapper.class);
    private final PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(placeHistoryMapper);
    private final PlaceController placeController = new PlaceController(eventBus);

    // housemate resources
    private final GWTProxyFeatureFactory featureFactory = new BootstrapFeatureFactory();

    // ui
    private LoginPopup loginPopup = null;
    private Page page = null;
    private com.intuso.housemate.web.client.bootstrap.view.UsersView userView = null;
    private com.intuso.housemate.web.client.bootstrap.view.DevicesView deviceView = null;
    private com.intuso.housemate.web.client.bootstrap.view.AutomationsView automationView = null;


    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public HousemateActivityMapper getActivityMapper() {
        return activityMapper;
    }

    @Override
    public HousematePlaceHistoryMapper getPlaceHistoryMapper() {
        return placeHistoryMapper;
    }

    @Override
    public PlaceHistoryHandler getPlaceHistoryHandler() {
        return placeHistoryHandler;
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public GWTProxyFeatureFactory getFeatureFactory() {
        return featureFactory;
    }

    @Override
    public LoginView getLoginView() {
        if(loginPopup == null)
            loginPopup = new LoginPopup();
        return loginPopup;
    }

    @Override
    public Page getPage() {
        if(page == null)
            page = new Page();
        return page;
    }

    @Override
    public UsersView getUsersView() {
        if(userView == null)
            userView = new com.intuso.housemate.web.client.bootstrap.view.UsersView();
        return userView;
    }

    @Override
    public DevicesView getDevicesView() {
        if(deviceView == null)
            deviceView = new com.intuso.housemate.web.client.bootstrap.view.DevicesView();
        return deviceView;
    }

    @Override
    public AutomationsView getAutomationsView() {
        if(automationView == null)
            automationView = new com.intuso.housemate.web.client.bootstrap.view.AutomationsView();
        return automationView;
    }
}
