package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class TypeInputEditedEvent extends GwtEvent<TypeInputEditedHandler> {

    public static Type<TypeInputEditedHandler> TYPE = new Type<TypeInputEditedHandler>();

    private TypeInstance newValue;

    public TypeInputEditedEvent(TypeInstance newValue) {
        this.newValue = newValue;
    }

    public TypeInstance getNewValue() {
        return newValue;
    }

    @Override
    public Type<TypeInputEditedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TypeInputEditedHandler handler) {
        handler.onTypeInputEdited(this);
    }
}
