package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.RouterRoot;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.place.DevicesPlace;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Housemate implements EntryPoint {

    public final static ApplicationDetails APPLICATION_DETAILS = new ApplicationDetails("com.intuso.housemate.web.client", "Housemate Web Interface", "Housemate Web Interface");
    public final static GWTGinjector INJECTOR = GWT.create(GWTGinjector.class);

    private final RootListener<RouterRoot> routerRootListener = new RootListener<RouterRoot>() {

        @Override
        public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
            if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently) {
                resetContent();
                INJECTOR.getRouter().connect();
            }
        }

        @Override
        public void applicationStatusChanged(RouterRoot root, ApplicationStatus applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
            if(applicationInstanceStatus != ApplicationInstanceStatus.Allowed)
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

    private final RootListener<GWTProxyRoot> proxyRootListener = new RootListener<GWTProxyRoot>() {
        @Override
        public void serverConnectionStatusChanged(GWTProxyRoot root, ServerConnectionStatus serverConnectionStatus) {
            // do nothing
        }

        @Override
        public void applicationStatusChanged(GWTProxyRoot root, ApplicationStatus applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(GWTProxyRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
            if (applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                INJECTOR.getProxyRoot().clearLoadedObjects();
                INJECTOR.getProxyRoot().loadChildOverviews();
                List<HousemateObject.TreeLoadInfo> loadInfos = Lists.newArrayList();
                loadInfos.add(new HousemateObject.TreeLoadInfo(Root.TYPES_ID, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
                INJECTOR.getProxyRoot().load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(HousemateObject.TreeLoadInfo tl) {
                        // todo show error
                    }

                    @Override
                    public void allLoaded() {
                        // add the main view to the root panel of the page
                        RootPanel.get().add(INJECTOR.getPage());
                        // force the page to reload by going to a "new" place
                        INJECTOR.getPlaceController().goTo(INJECTOR.getPlaceHistoryMapper().getPlace(History.getToken()));
                    }
                }, "webInitialLoad", loadInfos));
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
