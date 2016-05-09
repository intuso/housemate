package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class PerformCommandHandler implements EventHandler {

    public final static PerformCommandHandler HANDLER = new PerformCommandHandler();

    private Command.PerformListener<GWTProxyCommand> commandPerformListener = new Command.PerformListener<GWTProxyCommand>() {

        @Override
        public void commandStarted(GWTProxyCommand command) {
            // TODO show in progress
        }

        @Override
        public void commandFinished(GWTProxyCommand command) {
            // TODO show success
        }

        @Override
        public void commandFailed(GWTProxyCommand command, String error) {
            Window.alert("Command " + command.getName() + " failed to complete: " + error);
        }
    };

    public void performCommand(GWTProxyCommand command, Type.InstanceMap values, Command.PerformListener<GWTProxyCommand> commandPerformListener) {
        command.perform(values, commandPerformListener != null ? commandPerformListener : this.commandPerformListener);
    }
}
