package com.intuso.housemate.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.web.client.handler.PerformCommandHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class PerformCommandEvent extends GwtEvent<PerformCommandHandler> {

    public static Type<PerformCommandHandler> TYPE = new Type<>();

    private GWTProxyCommand command;
    private com.intuso.housemate.client.v1_0.api.object.Type.InstanceMap values;
    private Command.PerformListener<GWTProxyCommand> commandPerformListener;

    public PerformCommandEvent(GWTProxyCommand command, com.intuso.housemate.client.v1_0.api.object.Type.InstanceMap values) {
        this(command, values, null);
    }

    public PerformCommandEvent(GWTProxyCommand command, com.intuso.housemate.client.v1_0.api.object.Type.InstanceMap values, Command.PerformListener<GWTProxyCommand> commandPerformListener) {
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
