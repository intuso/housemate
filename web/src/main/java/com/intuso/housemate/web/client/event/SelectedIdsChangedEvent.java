package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;

import java.util.Set;

/**
 */
public class SelectedIdsChangedEvent extends GwtEvent<SelectedIdsChangedHandler> {

    public final static Type<SelectedIdsChangedHandler> TYPE = new Type<SelectedIdsChangedHandler>();

    private final Set<String> ids;

    public SelectedIdsChangedEvent(Set<String> ids) {
        this.ids = ids;
    }

    @Override
    public Type<SelectedIdsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectedIdsChangedHandler handler) {
        handler.selectedIdsChanged(ids);
    }
}
