package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;

/**
 */
public interface ObjectSelectedHandler extends EventHandler {
    void objectSelected(ProxyObject<?, ?, ?, ?, ?> object, ProxyObject<?, ?, ?, ?, ?> parent, ChildOverview childOverview);
}
