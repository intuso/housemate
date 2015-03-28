package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;

/**
 */
public class UserInputEvent extends GwtEvent<UserInputHandler> {

    public final static Type<UserInputHandler> TYPE = new Type<>();

    public UserInputEvent() {}

    @Override
    public Type<UserInputHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserInputHandler handler) {
        handler.onUserInput(this);
    }
}
