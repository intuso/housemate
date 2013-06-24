package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;

/**
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
