package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 */
public class ObjectSelectedEvent<O extends ProxyObject<?, ?, ?, ?, ?>> extends GwtEvent<ObjectSelectedHandler> {

    public final static Type<ObjectSelectedHandler> TYPE = new Type<ObjectSelectedHandler>();

    private final O object;

    public ObjectSelectedEvent(O object) {
        this.object = object;
    }

    @Override
    public Type<ObjectSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ObjectSelectedHandler handler) {
        handler.objectSelected(object);
    }

    public O getObject() {
        return object;
    }
}
