package com.intuso.housemate.web.client.comms;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.ClientRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.RouterRoot;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.housemate.web.client.ui.view.LoginView;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class LoginManager implements CredentialsSubmittedHandler, ClientRoot.Listener<RouterRoot> {

    private final static String INSTANCE_ID = "application.instance.id";

    private final CommsServiceAsync commsService;

    private final Log log;
    private final PropertyRepository properties;
    private final LoginView loginView;
    private final Router router;
    private final GWTProxyRoot proxyRoot;

    private boolean connectedToServer = false;

    @Inject
    public LoginManager(Log log, PropertyRepository properties, LoginView loginView, Router router, EventBus eventBus,
                        GWTProxyRoot proxyRoot, CommsServiceAsync commsService) {
        this.log = log;
        this.properties = properties;
        this.loginView = loginView;
        this.router = router;
        this.proxyRoot = proxyRoot;
        this.commsService = commsService;
        eventBus.addHandler(CredentialsSubmittedEvent.TYPE, this);
        router.addObjectListener(this);
    }

    @Override
    public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
        boolean connectedToServer = serverConnectionStatus == ServerConnectionStatus.ConnectedToServer
                || serverConnectionStatus == ServerConnectionStatus.DisconnectedTemporarily;
        if(this.connectedToServer != connectedToServer) {
            this.connectedToServer = connectedToServer;
            if(connectedToServer)
                login();
        }
    }

    @Override
    public void applicationStatusChanged(RouterRoot root, Application.Status applicationStatus) {

    }

    @Override
    public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstance.Status applicationInstanceStatus) {
        if(applicationInstanceStatus == ApplicationInstance.Status.Allowed)
            proxyRoot.register(Housemate.APPLICATION_DETAILS, "UI");
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
            router.register(Housemate.APPLICATION_DETAILS, "UI");
        else {
            loginView.setMessage(null);
            loginView.show();
            loginView.enable();
        }
    }

    public void logout() {
        properties.remove(INSTANCE_ID);
        router.unregister();
    }

    @Override
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event) {
        loginView.disable();
        commsService.checkCredentials(event.getUsername(), event.getPassword(), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                log.e("Failed to check credentials", caught);
                loginView.setMessage("Failed to check credentials");
                loginView.enable();
            }

            @Override
            public void onSuccess(Boolean result) {
                loginView.enable();
                if(result) {
                    router.register(Housemate.APPLICATION_DETAILS, "UI");
                    loginView.hide();
                } else {
                    loginView.setMessage("Incorrect credentials");
                }
            }
        });
    }
}
