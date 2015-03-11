package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 *
 * Interface to show that the implementing object has a list of object selected handlers
 */
public interface HasUserInputHandlers {

    /**
     * Add a user input handler
     * @param handler the handler to add
     * @return a handler registration
     */
    HandlerRegistration addUserInputHandler(UserInputHandler handler);
}
