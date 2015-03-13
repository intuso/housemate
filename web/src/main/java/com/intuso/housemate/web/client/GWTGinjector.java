package com.intuso.housemate.web.client;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.web.client.bootstrap.BootstrapUiModule;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;
import com.intuso.housemate.web.client.ui.view.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/01/14
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
@GinModules({MainModule.class, BootstrapUiModule.class})
public interface GWTGinjector extends Ginjector {

    // general things
    EventBus getEventBus();
    LoginManager getLoginManager();
    Router getRouter();
    GWTProxyRoot getProxyRoot();
    HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> getObjectFactory();
    ProxyFeatureFactory<GWTProxyFeature, GWTProxyDevice> getFeatureFactory();

    // activity/place stuff
    ActivityMapper getActivityMapper();
    PlaceHistoryMapper getPlaceHistoryMapper();
    PlaceHistoryHandler getPlaceHistoryHandler();
    PlaceController getPlaceController();

    // UI views
    LoginView getLoginView();
    Page getPage();
    DevicesView getDevicesView();
    AutomationsView getAutomationsView();
    UsersView getUsersView();
    HardwaresView getHardwaresView();
    ApplicationsView getApplicationsView();
}
