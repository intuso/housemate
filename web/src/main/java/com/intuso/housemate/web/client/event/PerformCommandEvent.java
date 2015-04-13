package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class PerformCommandEvent extends GwtEvent<PerformCommandHandler> {

    public static Type<PerformCommandHandler> TYPE = new Type<>();

    private GWTProxyCommand command;
    private TypeInstanceMap values;
    private CommandPerformListener<GWTProxyCommand> commandPerformListener;

    public PerformCommandEvent(GWTProxyCommand command, TypeInstanceMap values) {
        this(command, values, null);
    }

    public PerformCommandEvent(GWTProxyCommand command, TypeInstanceMap values, CommandPerformListener<GWTProxyCommand> commandPerformListener) {
        this.command = command;
        this.values = values;
        this.commandPerformListener = commandPerformListener;
    }

    @Override
    public Type<PerformCommandHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PerformCommandHandler handler) {
        handler.performCommand(command, values, commandPerformListener);
    }
}
