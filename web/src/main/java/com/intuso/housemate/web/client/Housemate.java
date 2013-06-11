package com.intuso.housemate.web.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.proxy.ProxyRootListener;
import com.intuso.housemate.web.client.event.LoggedInEvent;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.LoggedInHandler;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.client.service.CommsServiceAsync;

import java.util.HashMap;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Housemate implements EntryPoint, LoggedInHandler, ProxyRootListener<GWTProxyRootObject> {

    public static GWTEnvironment ENVIRONMENT;
    public final static ClientFactory FACTORY = GWT.create(ClientFactory.class);

    /**
    * This is the entry point method.
    */
    @Override
    public void onModuleLoad() {

        // tell the server that a new client has started
        CommsServiceAsync commsService = GWT.create(CommsService.class);
        commsService.clientStarted(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Could not connect to server. Please try again later");
            }

            @Override
            public void onSuccess(Void aVoid) {

                // we must start the platform and create the root object before anything tries to use it
                ENVIRONMENT = new GWTEnvironment(new HashMap<String, String>());
                ENVIRONMENT.getResources().setRoot(new GWTProxyRootObject(ENVIRONMENT.getResources(), ENVIRONMENT.getResources()));
                ENVIRONMENT.getResources().getLoginManager().init();

                // setup the activity manager
                ActivityManager activityManager = new ActivityManager(FACTORY.getActivityMapper(), FACTORY.getEventBus());
                activityManager.setDisplay(FACTORY.getPage().getContainer());

                // set up the history handler
                FACTORY.getPlaceHistoryHandler().register(FACTORY.getPlaceController(), FACTORY.getEventBus(), new HomePlace());

                // add our default handlers
                FACTORY.getEventBus().addHandler(PerformCommandEvent.TYPE, PerformCommandHandler.HANDLER);
                FACTORY.getEventBus().addHandler(LoggedInEvent.TYPE, Housemate.this);
                ENVIRONMENT.getResources().getRoot().addObjectListener(Housemate.this);

                // start the first login
                ENVIRONMENT.getResources().getLoginManager().login();
            }
        });
    }

    @Override
    public void statusChanged(GWTProxyRootObject root, Root.Status status) {
        // todo show status in the UI
    }

    @Override
    public void loaded() {
        FACTORY.getPlaceHistoryHandler().handleCurrentHistory();
    }

    @Override
    public void onLoginEvent(final LoggedInEvent event) {
        if(event.isLoggedIn()) {
            // add the main view to the root panel of the page
            RootPanel.get().add(FACTORY.getPage());
            // connect the root object
            ENVIRONMENT.getResources().getRoot().connect(
                    ENVIRONMENT.getResources().getLoginManager().getLoginMethod());
        } else {
            RootPanel.get().clear();
            Housemate.ENVIRONMENT.getResources().getLoginManager().login();
        }
    }
}
