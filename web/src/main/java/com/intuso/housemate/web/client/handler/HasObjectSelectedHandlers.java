package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 *
 * Interface to show that the implementing object has a list of object selected handlers
 */
public interface HasObjectSelectedHandlers<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> {

    /**
     * Add an object selected handler
     * @param handler the handler to add
     * @return a handler registration
     */
    HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler<O> handler);
}
