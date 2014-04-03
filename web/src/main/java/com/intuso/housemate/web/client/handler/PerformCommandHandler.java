package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;

/**
 */
public class PerformCommandHandler implements EventHandler {

    public final static PerformCommandHandler HANDLER = new PerformCommandHandler();

    private CommandPerformListener<Command<?, ?, ?>> commandListener = new CommandPerformListener<Command<?, ?, ?>>() {

        @Override
        public void commandStarted(Command<?, ?, ?> command) {
            // TODO show in progress
        }

        @Override
        public void commandFinished(Command<?, ?, ?> command) {
            // TODO show success
        }

        @Override
        public void commandFailed(Command<?, ?, ?> command, String error) {
            Window.alert("Command " + command.getName() + " failed to complete: " + error);
        }
    };

    public void performCommand(Command<?, ?, ?> command, TypeInstanceMap values) {
        command.perform(values, commandListener);
    }
}
