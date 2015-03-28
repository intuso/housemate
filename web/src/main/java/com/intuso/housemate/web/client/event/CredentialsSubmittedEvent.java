package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.web.client.handler.CredentialsSubmittedHandler;

/**
 */
public class CredentialsSubmittedEvent extends GwtEvent<CredentialsSubmittedHandler> {

    public static Type<CredentialsSubmittedHandler> TYPE = new Type<>();

    private final String username;
    private final String password;

    public CredentialsSubmittedEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Type<CredentialsSubmittedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CredentialsSubmittedHandler handler) {
        handler.onCredentialsSubmitted(this);
    }
}
