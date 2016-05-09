package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyRoot;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.Router;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.client.v1_0.data.api.access.ApplicationDetails;
import com.intuso.housemate.client.v1_0.data.api.access.ConnectionStatus;
import com.intuso.housemate.client.v1_0.api.Application;
import com.intuso.housemate.client.v1_0.api.ApplicationInstance;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.place.DevicesPlace;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Housemate implements EntryPoint {

    public final static ApplicationDetails APPLICATION_DETAILS = new ApplicationDetails("com.intuso.housemate.web.client", "Housemate Web Interface", "Housemate Web Interface");
    public final static GWTGinjector INJECTOR = GWT.create(GWTGinjector.class);

    private final Router.Listener<Router> routerRootListener = new Router.Listener<Router>() {

        @Override
        public void serverConnectionStatusChanged(Router root, ConnectionStatus connectionStatus) {
            if(connectionStatus == ConnectionStatus.DisconnectedPermanently) {
                resetContent();
                INJECTOR.getRouter().connect();
            }
        }

        @Override
        public void newServerInstance(Router root, String serverId) {
            resetContent();
        }
    };

    private final ProxyRoot.Listener<GWTProxyRoot> proxyRootListener = new ProxyRoot.Listener<GWTProxyRoot>() {

        @Override
        public void applicationStatusChanged(GWTProxyRoot root, Application.Status applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(GWTProxyRoot root, ApplicationInstance.Status applicationInstanceStatus) {
            if (applicationInstanceStatus == ApplicationInstance.Status.Allowed) {
                INJECTOR.getProxyRoot().clearLoadedObjects();
                TreeLoadInfo serverLoadInfo = new TreeLoadInfo("*");
                serverLoadInfo.getChildren().put(Server.Data.APPLICATIONS_ID, new TreeLoadInfo(Server.Data.APPLICATIONS_ID));
                serverLoadInfo.getChildren().put(Server.Data.AUTOMATIONS_ID, new TreeLoadInfo(Server.Data.AUTOMATIONS_ID));
                serverLoadInfo.getChildren().put(Server.Data.DEVICES_ID, new TreeLoadInfo(Server.Data.DEVICES_ID));
                serverLoadInfo.getChildren().put(Server.Data.HARDWARES_ID, new TreeLoadInfo(Server.Data.HARDWARES_ID));
                serverLoadInfo.getChildren().put(Server.Data.TYPES_ID, new TreeLoadInfo(Server.Data.TYPES_ID));
                serverLoadInfo.getChildren().put(Server.Data.USERS_ID, new TreeLoadInfo(Server.Data.USERS_ID));
                serverLoadInfo.getChildren().put(Server.Data.ADD_AUTOMATION_ID, new TreeLoadInfo(Server.Data.ADD_AUTOMATION_ID, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                serverLoadInfo.getChildren().put(Server.Data.ADD_DEVICE_ID, new TreeLoadInfo(Server.Data.ADD_DEVICE_ID, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                serverLoadInfo.getChildren().put(Server.Data.ADD_HARDWARE_ID, new TreeLoadInfo(Server.Data.ADD_HARDWARE_ID, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                serverLoadInfo.getChildren().put(Server.Data.ADD_USER_ID, new TreeLoadInfo(Server.Data.ADD_USER_ID, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                TreeLoadInfo serversLoadInfo = new TreeLoadInfo(ProxyRoot.SERVERS_ID, serverLoadInfo);
                INJECTOR.getProxyRoot().load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(List<String> errors) {
                        INJECTOR.getProxyRoot().getLogger().error("Failed to load clients");
                        // todo show error
                    }

                    @Override
                    public void succeeded() {
                        // add the main view to the root panel of the page
                        RootPanel.get().add(INJECTOR.getPage());
                        // force the page to reload by going to a "new" place
                        Place place = INJECTOR.getPlaceHistoryMapper().getPlace(History.getToken());
                        if(place == null)
                            place = new DevicesPlace();
                        INJECTOR.getPlaceController().goTo(place);
                    }
                }, Lists.newArrayList(serversLoadInfo)));
            }
        }

        @Override
        public void newApplicationInstance(GWTProxyRoot root, String instanceId) {
            // do nothing
        }
    };

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        // set up activity/history framework
        ActivityManager activityManager = new ActivityManager(INJECTOR.getActivityMapper(), INJECTOR.getEventBus());
        activityManager.setDisplay(INJECTOR.getPage().getContainer());
        INJECTOR.getPlaceHistoryHandler().register(INJECTOR.getPlaceController(), INJECTOR.getEventBus(), new DevicesPlace());

        // setup our HM stuff
        INJECTOR.getEventBus().addHandler(PerformCommandEvent.TYPE, PerformCommandHandler.HANDLER);

        // get the login manager - it's constructor registers it as a listener to the router root
        INJECTOR.getLoginManager();

        // connect the router. Add listeners so everything else happens automatically after
        INJECTOR.getRouter().addListener(routerRootListener);
        INJECTOR.getProxyRoot().addObjectListener(proxyRootListener);

        resetContent();

        INJECTOR.getRouter().connect();
    }

    private void resetContent() {
        RootPanel.get().remove(INJECTOR.getPage());
        INJECTOR.getProxyRoot().uninit();
        INJECTOR.getProxyRoot().init(null); // we don't really want to uninit all the normal listeners etc
    }
}
