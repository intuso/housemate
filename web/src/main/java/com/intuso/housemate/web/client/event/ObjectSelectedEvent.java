package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
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
