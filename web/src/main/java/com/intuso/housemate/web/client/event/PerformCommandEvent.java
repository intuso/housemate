package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.type.TypeValues;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class PerformCommandEvent extends GwtEvent<PerformCommandHandler> {

    public static Type<PerformCommandHandler> TYPE = new Type<PerformCommandHandler>();

    private Command<?, ?> command;
    private TypeValues values;

    public PerformCommandEvent(Command<?, ?> command, TypeValues values) {
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
