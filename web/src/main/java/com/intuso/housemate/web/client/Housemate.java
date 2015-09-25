package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.ClientRoot;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.RouterRoot;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.comms.v1_0.api.payload.ServerData;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
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

    private final ClientRoot.Listener<RouterRoot> routerRootListener = new ClientRoot.Listener<RouterRoot>() {

        @Override
        public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
            if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently) {
                resetContent();
                INJECTOR.getRouter().connect();
            }
        }

        @Override
        public void applicationStatusChanged(RouterRoot root, Application.Status applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstance.Status applicationInstanceStatus) {
            if(applicationInstanceStatus != ApplicationInstance.Status.Allowed)
                resetContent();
        }

        @Override
        public void newApplicationInstance(RouterRoot root, String instanceId) {
            // do nothing
        }

        @Override
        public void newServerInstance(RouterRoot root, String serverId) {
            resetContent();
        }
    };

    private final ClientRoot.Listener<GWTProxyRoot> proxyRootListener = new ClientRoot.Listener<GWTProxyRoot>() {
        @Override
        public void serverConnectionStatusChanged(GWTProxyRoot root, ServerConnectionStatus serverConnectionStatus) {
            // do nothing
        }

        @Override
        public void applicationStatusChanged(GWTProxyRoot root, Application.Status applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(GWTProxyRoot root, ApplicationInstance.Status applicationInstanceStatus) {
            if (applicationInstanceStatus == ApplicationInstance.Status.Allowed) {
                INJECTOR.getProxyRoot().clearLoadedObjects();
                INJECTOR.getProxyRoot().loadChildOverviews();
                RemoteObject.TreeLoadInfo clientLoadInfo = new RemoteObject.TreeLoadInfo("*");
                clientLoadInfo.getChildren().put(ServerData.APPLICATIONS_ID, new RemoteObject.TreeLoadInfo(ServerData.APPLICATIONS_ID));
                clientLoadInfo.getChildren().put(ServerData.AUTOMATIONS_ID, new RemoteObject.TreeLoadInfo(ServerData.AUTOMATIONS_ID));
                clientLoadInfo.getChildren().put(ServerData.DEVICES_ID, new RemoteObject.TreeLoadInfo(ServerData.DEVICES_ID));
                clientLoadInfo.getChildren().put(ServerData.HARDWARES_ID, new RemoteObject.TreeLoadInfo(ServerData.HARDWARES_ID));
                clientLoadInfo.getChildren().put(ServerData.TYPES_ID, new RemoteObject.TreeLoadInfo(ServerData.TYPES_ID));
                clientLoadInfo.getChildren().put(ServerData.USERS_ID, new RemoteObject.TreeLoadInfo(ServerData.USERS_ID));
                clientLoadInfo.getChildren().put(ServerData.ADD_AUTOMATION_ID, new RemoteObject.TreeLoadInfo(ServerData.ADD_AUTOMATION_ID, new RemoteObject.TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                clientLoadInfo.getChildren().put(ServerData.ADD_DEVICE_ID, new RemoteObject.TreeLoadInfo(ServerData.ADD_DEVICE_ID, new RemoteObject.TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                clientLoadInfo.getChildren().put(ServerData.ADD_HARDWARE_ID, new RemoteObject.TreeLoadInfo(ServerData.ADD_HARDWARE_ID, new RemoteObject.TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                clientLoadInfo.getChildren().put(ServerData.ADD_USER_ID, new RemoteObject.TreeLoadInfo(ServerData.ADD_USER_ID, new RemoteObject.TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
                RemoteObject.TreeLoadInfo clientsLoadInfo = new RemoteObject.TreeLoadInfo(ProxyRoot.SERVERS_ID, clientLoadInfo);
                INJECTOR.getProxyRoot().load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(List<String> errors) {
                        INJECTOR.getProxyRoot().getLog().e("Failed to load clients");
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
                }, Lists.newArrayList(clientsLoadInfo)));
            }
        }

        @Override
        public void newApplicationInstance(GWTProxyRoot root, String instanceId) {
            // do nothing
        }

        @Override
        public void newServerInstance(GWTProxyRoot root, String serverId) {
            // do nothing (handled by router listener
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
        INJECTOR.getRouter().addObjectListener(routerRootListener);
        INJECTOR.getProxyRoot().addObjectListener(proxyRootListener);
        INJECTOR.getRouter().connect();
    }

    private void resetContent() {
        RootPanel.get().remove(INJECTOR.getPage());
        INJECTOR.getProxyRoot().uninit();
        INJECTOR.getProxyRoot().init(null); // we don't really want to uninit all the normal listeners etc
    }
}
