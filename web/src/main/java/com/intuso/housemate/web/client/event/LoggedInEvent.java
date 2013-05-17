package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.web.client.handler.LoggedInHandler;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class LoggedInEvent extends GwtEvent<LoggedInHandler> {

    public static Type<LoggedInHandler> TYPE = new Type<LoggedInHandler>();

    private final boolean isLoggedIn;

    public LoggedInEvent(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public Type<LoggedInHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoggedInHandler handler) {
        handler.onLoginEvent(this);
    }
}
