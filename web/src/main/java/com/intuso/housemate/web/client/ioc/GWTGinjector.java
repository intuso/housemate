package com.intuso.housemate.web.client.ioc;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.web.client.bootstrap.ioc.BootstrapUiModule;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;
import com.intuso.housemate.web.client.ui.view.*;
import com.intuso.utilities.object.ObjectFactory;

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
    Router<?> getRouter();
    GWTProxyRoot getProxyRoot();
    ObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> getObjectFactory();
    ProxyFeatureFactory<GWTProxyFeature, GWTProxyDevice> getFeatureFactory();

    // activity/place stuff
    ActivityMapper getActivityMapper();
    PlaceHistoryMapper getPlaceHistoryMapper();
    PlaceHistoryHandler getPlaceHistoryHandler();
    PlaceController getPlaceController();

    // UI views
    LoginView getLoginView();
    Page getPage();
    ApplicationsView getApplicationsView();
    AutomationsView getAutomationsView();
    DevicesView getDevicesView();
    HardwaresView getHardwaresView();
    ServersView getServersView();
    UsersView getUsersView();
}
