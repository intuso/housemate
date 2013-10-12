package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;

import java.util.Set;

/**
 */
public interface SelectedIdsChangedHandler extends EventHandler {
    public void selectedIdsChanged(Set<String> ids);
}
