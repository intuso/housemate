package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentEditedEvent extends GwtEvent<ArgumentEditedHandler> {

    public static Type<ArgumentEditedHandler> TYPE = new Type<ArgumentEditedHandler>();

    private String newValue;

    public ArgumentEditedEvent(String newValue) {
        this.newValue = newValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public Type<ArgumentEditedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ArgumentEditedHandler handler) {
        handler.onArgumentEdited(this);
    }
}
