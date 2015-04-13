package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class PerformCommandHandler implements EventHandler {

    public final static PerformCommandHandler HANDLER = new PerformCommandHandler();

    private CommandPerformListener<GWTProxyCommand> commandPerformListener = new CommandPerformListener<GWTProxyCommand>() {

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

    public void performCommand(GWTProxyCommand command, TypeInstanceMap values, CommandPerformListener<GWTProxyCommand> commandPerformListener) {
        command.perform(values, commandPerformListener != null ? commandPerformListener : this.commandPerformListener);
    }
}
