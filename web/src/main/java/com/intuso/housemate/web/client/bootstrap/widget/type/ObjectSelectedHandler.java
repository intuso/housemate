package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 */
public interface ObjectSelectedHandler<O extends ProxyObject<?, ?, ?, ?, ?>> extends EventHandler {
    public void objectSelected(O object);
}
