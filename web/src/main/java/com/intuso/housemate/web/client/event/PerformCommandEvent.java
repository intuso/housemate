package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;

/**
 */
public class PerformCommandEvent extends GwtEvent<PerformCommandHandler> {

    public static Type<PerformCommandHandler> TYPE = new Type<PerformCommandHandler>();

    private Command<?, ?, ?> command;
    private TypeInstanceMap values;

    public PerformCommandEvent(Command<?, ?, ?> command, TypeInstanceMap values) {
        this.command = command;
        this.values = values;
    }

    @Override
    public Type<PerformCommandHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PerformCommandHandler handler) {
        handler.performCommand(command, values);
    }
}
