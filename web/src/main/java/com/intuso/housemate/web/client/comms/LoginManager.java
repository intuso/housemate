package com.intuso.housemate.web.client.comms;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.ui.view.LoginView;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class LoginManager implements CredentialsSubmittedHandler, RootListener<RouterRoot> {

    private final static String INSTANCE_ID = "application.instance.id";

    private final PropertyRepository properties;
    private final LoginView loginView;
    private final Router router;
    private final GWTProxyRoot proxyRoot;
    private boolean isLoggedIn = false;
    private boolean loginAttempt = false;

    @Inject
    public LoginManager(PropertyRepository properties, LoginView loginView, Router router, EventBus eventBus, GWTProxyRoot proxyRoot) {
        this.properties = properties;
        this.loginView = loginView;
        this.router = router;
        this.proxyRoot = proxyRoot;
        eventBus.addHandler(CredentialsSubmittedEvent.TYPE, this);
        router.addObjectListener(this);
    }

    @Override
    public void statusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
        if(serverConnectionStatus == ServerConnectionStatus.ConnectedToServer) {
            switch (applicationInstanceStatus) {
                case Allowed:
                    isLoggedIn = true;
                    loginView.hide();
                    proxyRoot.register(Housemate.APPLICATION_DETAILS);
                    break;
                case Unregistered:
                    if(loginAttempt)
                        loginView.show("Incorrect credentials");
                    else
                        login();
                    loginView.enable();
                    break;
            }
        }
    }

    @Override
    public void newApplicationInstance(RouterRoot root, String instanceId) {
        // connection manager saves this in the properties (cookie-backed) for us
    }

    @Override
    public void newServerInstance(RouterRoot root, String serverId) {
        // todo display a notification that the data is being reloaded
    }

    private void login() {
        if(properties.keySet().contains(INSTANCE_ID))
            router.register(Housemate.APPLICATION_DETAILS);
        else {
            loginAttempt = false;
            loginView.show(null);
            loginView.enable();
        }
    }

    public void logout() {
        properties.remove(INSTANCE_ID);
        loginAttempt = false;
        isLoggedIn = false;
        router.unregister();
        loginView.show(null);
        loginView.enable();
    }

    @Override
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event) {
        loginAttempt = true;
        properties.set(INSTANCE_ID, event.getUsername());
        if(!isLoggedIn)
            router.register(Housemate.APPLICATION_DETAILS);
    }
}
