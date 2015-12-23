package com.intuso.housemate.web.client.comms;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.housemate.web.client.ui.view.LoginView;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;

/**
 */
public class LoginManager implements CredentialsSubmittedHandler, Router.Listener<Router> {

    private final static String INSTANCE_ID = "application.instance.id";

    private final CommsServiceAsync commsService;

    private final Logger logger;
    private final PropertyRepository properties;
    private final LoginView loginView;
    private final GWTProxyRoot proxyRoot;

    private boolean connectedToServer = false;

    @Inject
    public LoginManager(Logger logger, PropertyRepository properties, LoginView loginView, Router<?> router, EventBus eventBus,
                        GWTProxyRoot proxyRoot, CommsServiceAsync commsService) {
        this.logger = logger;
        this.properties = properties;
        this.loginView = loginView;
        this.proxyRoot = proxyRoot;
        this.commsService = commsService;
        eventBus.addHandler(CredentialsSubmittedEvent.TYPE, this);
        router.addListener(this);
    }

    @Override
    public void serverConnectionStatusChanged(Router root, ConnectionStatus connectionStatus) {
        boolean connectedToServer = connectionStatus == ConnectionStatus.ConnectedToServer
                || connectionStatus == ConnectionStatus.DisconnectedTemporarily;
        if(this.connectedToServer != connectedToServer) {
            this.connectedToServer = connectedToServer;
            if(connectedToServer)
                login();
        }
    }

    @Override
    public void newServerInstance(Router root, String serverId) {
        // todo display a notification that the data is being reloaded
    }

    private void login() {
        if(properties.keySet().contains(INSTANCE_ID))
            proxyRoot.register(Housemate.APPLICATION_DETAILS);
        else {
            loginView.setMessage(null);
            loginView.show();
            loginView.enable();
        }
    }

    public void logout() {
        properties.remove(INSTANCE_ID);
        proxyRoot.unregister();
    }

    @Override
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event) {
        loginView.disable();
        commsService.checkCredentials(event.getUsername(), event.getPassword(), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.error("Failed to check credentials", caught);
                loginView.setMessage("Failed to check credentials");
                loginView.enable();
            }

            @Override
            public void onSuccess(Boolean result) {
                loginView.enable();
                if(result) {
                    proxyRoot.register(Housemate.APPLICATION_DETAILS);
                    loginView.hide();
                } else {
                    loginView.setMessage("Incorrect credentials");
                }
            }
        });
    }
}
