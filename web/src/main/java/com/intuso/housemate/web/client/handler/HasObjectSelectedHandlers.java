package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 06/12/12
 * Time: 00:05
 * To change this template use File | Settings | File Templates.
 */
public interface HasObjectSelectedHandlers<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
    HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler<O> handler);
}
