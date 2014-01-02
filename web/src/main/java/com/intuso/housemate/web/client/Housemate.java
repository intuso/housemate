package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.housemate.web.client.place.DevicesPlace;

import java.util.HashMap;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Housemate implements EntryPoint, RootListener<RouterRootObject> {

    public static GWTEnvironment ENVIRONMENT;
    public final static ClientFactory FACTORY = GWT.create(ClientFactory.class);

    /**
    * This is the entry point method.
    */
    @Override
    public void onModuleLoad() {

        // we must start the platform and connect the router first
        ENVIRONMENT = new GWTEnvironment(new HashMap<String, String>(), FACTORY.getFeatureFactory(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed to connect to server. Please try again later");
            }

            @Override
            public void onSuccess(Void result) {
                // setup the activity manager
                ActivityManager activityManager = new ActivityManager(FACTORY.getActivityMapper(), FACTORY.getEventBus());
                activityManager.setDisplay(FACTORY.getPage().getContainer());

                // setup the history handler
                FACTORY.getPlaceHistoryHandler().register(FACTORY.getPlaceController(), FACTORY.getEventBus(), new DevicesPlace());

                // setup our HM stuff
                ENVIRONMENT.getResources().getLoginManager().init();
                ENVIRONMENT.getResources().getRouter().addObjectListener(Housemate.this);
                FACTORY.getEventBus().addHandler(PerformCommandEvent.TYPE, PerformCommandHandler.HANDLER);

                // start the first login
                ENVIRONMENT.getResources().getLoginManager().startLogin();
            }
        });
        ENVIRONMENT.getResources().getRouter().connect();
    }

    @Override
    public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
        switch (status) {
            case Authenticated:
                if(ENVIRONMENT.getResources().getRoot() != null)
                    ENVIRONMENT.getResources().getRoot().uninit();
                ENVIRONMENT.getResources().setRoot(new GWTProxyRootObject(ENVIRONMENT.getResources(), ENVIRONMENT.getResources()));
                // add the main view to the root panel of the page
                RootPanel.get().add(FACTORY.getPage());
                // connect the root object
                ENVIRONMENT.getResources().getRoot().addObjectListener(new RootListener<GWTProxyRootObject>() {
                    @Override
                    public void connectionStatusChanged(GWTProxyRootObject root, ConnectionStatus status) {
                        if(status == ConnectionStatus.Authenticated) {
                            List<HousemateObject.TreeLoadInfo> loadInfos = Lists.newArrayList();
                            for(String childId : new String[] {Root.TYPES_ID, Root.ADD_DEVICE_ID, Root.ADD_AUTOMATION_ID, Root.ADD_USER_ID})
                                loadInfos.add(new HousemateObject.TreeLoadInfo(childId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
                            root.load(new LoadManager("webUI", loadInfos) {
                                @Override
                                protected void failed(HousemateObject.TreeLoadInfo tl) {
                                    // todo show error
                                }

                                @Override
                                protected void allLoaded() {
                                    FACTORY.getPlaceHistoryHandler().handleCurrentHistory();
                                }
                            });
                        }
                    }

                    @Override
                    public void newServerInstance(GWTProxyRootObject root) {
                        // do nothing (handled by router listener
                    }
                });
                ENVIRONMENT.getResources().getRoot().login(ENVIRONMENT.getResources().getLoginManager().getLoginMethod());
                break;
            case Disconnected:
            case Unauthenticated:
                resetContent();
                break;
            case AuthenticationFailed:
            case Authenticating:
            case Connecting:
                break;
        }
    }

    @Override
    public void newServerInstance(RouterRootObject root) {
        resetContent();
    }

    private void resetContent() {
        RootPanel.get().remove(FACTORY.getPage());
        if(ENVIRONMENT.getResources().getRoot() != null) {
            ENVIRONMENT.getResources().getRoot().uninit();
            ENVIRONMENT.getResources().setRoot(null);
        }
    }
}
