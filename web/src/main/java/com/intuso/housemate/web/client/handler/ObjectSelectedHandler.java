package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;

/**
 */
public interface ObjectSelectedHandler<O extends HousemateObject<?, ?, ?, ?, ?>> extends EventHandler {
    public void objectSelected(ObjectSelectedEvent<O> event);
}
