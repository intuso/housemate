package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 *
 * Interface to show that the implementing object has a list of type input edited handlers
 */
public interface HasTypeInputEditedHandlers {

    /**
     * Add a type input edited handler
     * @param handler the handler to add
     * @return a handler registration
     */
    HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler);
}
