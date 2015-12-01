package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;

/**
 */
public class ObjectSelectedEvent extends GwtEvent<ObjectSelectedHandler> {

    public final static Type<ObjectSelectedHandler> TYPE = new Type<>();

    private final ProxyObject<?, ?, ?, ?, ?> object;
    private final ProxyObject<?, ?, ?, ?, ?> parent;
    private final ChildOverview childOverview;

    public ObjectSelectedEvent(ProxyObject<?, ?, ?, ?, ?> object, ProxyObject<?, ?, ?, ?, ?> parent, ChildOverview childOverview) {
        this.object = object;
        this.parent = parent;
        this.childOverview = childOverview;
    }

    @Override
    public Type<ObjectSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ObjectSelectedHandler handler) {
        handler.objectSelected(object, parent, childOverview);
    }

    public ProxyObject<?, ?, ?, ?, ?> getObject() {
        return object;
    }

    public ChildOverview getChildOverview() {
        return childOverview;
    }

    public ProxyObject<?, ?, ?, ?, ?> getParent() {
        return parent;
    }
}
