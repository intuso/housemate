package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.object.proxy.ProxyObject;

import java.util.Set;

/**
 */
public interface ObjectSelectedHandler<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> extends EventHandler {
    public void objectSelected(O object);
}
