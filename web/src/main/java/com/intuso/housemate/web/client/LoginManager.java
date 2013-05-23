package com.intuso.housemate.web.client;

import com.google.gwt.user.client.Cookies;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.authentication.Session;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.event.LoggedInEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/05/13
 * Time: 09:01
 * To change this template use File | Settings | File Templates.
 */
public class LoginManager implements CredentialsSubmittedHandler {

    private final static String SESSION_COOKIE = "session";

    private final AuthenticationResponseHandler responseHandler = new AuthenticationResponseHandler() {
        @Override
        public void responseReceived(Root.AuthenticationResponse response) {
            if(response.getUserId() != null) {
                isLoggedIn = true;
                Cookies.setCookie(SESSION_COOKIE, response.getConnectionId());
                Housemate.FACTORY.getEventBus().fireEvent(new LoggedInEvent(true));
                Housemate.FACTORY.getLoginView().hide();
            } else {
                Housemate.FACTORY.getLoginView().show("Incorrect credentials");
                Housemate.FACTORY.getLoginView().enable();
            }
        }
    };

    private boolean isLoggedIn = false;

    public void init() {
        Housemate.FACTORY.getEventBus().addHandler(CredentialsSubmittedEvent.TYPE, this);
    }

    public void login() {
        if(Cookies.getCookieNames().contains(SESSION_COOKIE))
            Housemate.ENVIRONMENT.getResources().getRouter().connect(
                    new Session(true, Cookies.getCookie(SESSION_COOKIE)), responseHandler);
        else {
            Housemate.FACTORY.getLoginView().show(null);
            Housemate.FACTORY.getLoginView().enable();
        }
    }

    public void logout() {
        Cookies.removeCookie(SESSION_COOKIE);
        isLoggedIn = false;
        Housemate.FACTORY.getEventBus().fireEvent(new LoggedInEvent(false));
    }

    @Override
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event) {
        if(!isLoggedIn)
            Housemate.ENVIRONMENT.getResources().getRouter().connect(
                    new UsernamePassword(true, event.getUsername(), event.getPassword(), true), responseHandler);
    }

    public AuthenticationMethod getLoginMethod() {
        return new Session(true, Cookies.getCookie(SESSION_COOKIE));
    }
}
