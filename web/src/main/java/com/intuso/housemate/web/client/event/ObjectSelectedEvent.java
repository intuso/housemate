package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 */
public class ObjectSelectedEvent<O extends HousemateObject<?, ?, ?, ?, ?>> extends GwtEvent<ObjectSelectedHandler> {

    public static Type<ObjectSelectedHandler> TYPE = new Type<ObjectSelectedHandler>();

    private O object;

    public ObjectSelectedEvent(O object) {
        this.object = object;
    }

    public O getObject() {
        return object;
    }

    @Override
    public Type<ObjectSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ObjectSelectedHandler handler) {
        handler.objectSelected(this);
    }
}
