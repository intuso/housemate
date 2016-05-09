package com.intuso.housemate.web.client.ioc;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.data.api.ObjectFactory;
import com.intuso.housemate.client.v1_0.data.api.Router;
import com.intuso.housemate.web.client.bootstrap.ioc.BootstrapUiModule;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
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
    Router<?> getRouter();
    GWTProxyRoot getProxyRoot();
    ObjectFactory<Object.Data<?>, ProxyObject<?, ?, ?, ?, ?>> getObjectFactory();

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
    UsersView getUsersView();
}
