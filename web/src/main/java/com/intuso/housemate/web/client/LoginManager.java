package com.intuso.housemate.web.client;

import com.google.gwt.user.client.Cookies;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.Session;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;

/**
 */
public class LoginManager implements CredentialsSubmittedHandler {

    private final static String SESSION_COOKIE = "session";

    private final RootListener<RouterRootObject> statusChangedListener = new RootListener<RouterRootObject>() {
        @Override
        public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
            switch (status) {
                case Authenticated:
                    isLoggedIn = true;
                    Cookies.setCookie(SESSION_COOKIE, root.getConnectionId());
                    Housemate.FACTORY.getLoginView().hide();
                    break;
                case AuthenticationFailed:
                    Housemate.FACTORY.getLoginView().show("Incorrect credentials");
                    Housemate.FACTORY.getLoginView().enable();
                    break;
            }
        }

        @Override
        public void newServerInstance(RouterRootObject root) {
            isLoggedIn = false;
            Housemate.FACTORY.getLoginView().show("The server restarted. Please login again");
            Housemate.FACTORY.getLoginView().enable();
        }
    };

    private boolean isLoggedIn = false;

    public void init() {
        Housemate.ENVIRONMENT.getResources().getRouter().addObjectListener(statusChangedListener);
        Housemate.FACTORY.getEventBus().addHandler(CredentialsSubmittedEvent.TYPE, this);
    }

    public void startLogin() {
        if(Cookies.getCookieNames().contains(SESSION_COOKIE))
            Housemate.ENVIRONMENT.getResources().getRouter().login(
                    new Session(Cookies.getCookie(SESSION_COOKIE)));
        else {
            Housemate.FACTORY.getLoginView().show(null);
            Housemate.FACTORY.getLoginView().enable();
        }
    }

    public void logout() {
        Cookies.removeCookie(SESSION_COOKIE);
        isLoggedIn = false;
        Housemate.ENVIRONMENT.getResources().getRouter().logout();
        Housemate.FACTORY.getLoginView().show(null);
        Housemate.FACTORY.getLoginView().enable();
    }

    @Override
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event) {
        if(!isLoggedIn)
            Housemate.ENVIRONMENT.getResources().getRouter().login(
                    new UsernamePassword(event.getUsername(), event.getPassword(), true));
    }

    public AuthenticationMethod getLoginMethod() {
        return new Session(Cookies.getCookie(SESSION_COOKIE));
    }
}
