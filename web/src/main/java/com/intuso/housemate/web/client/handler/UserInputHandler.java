package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.web.client.event.UserInputEvent;

/**
 */
public interface UserInputHandler extends EventHandler {
    public void onUserInput(UserInputEvent event);
}
