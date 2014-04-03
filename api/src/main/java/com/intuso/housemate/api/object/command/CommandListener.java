package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for command
 */
public interface CommandListener<C extends Command<?, ?, ?>>
        extends ObjectListener {

    public void commandEnabled(C command, boolean enabled);

    /**
     * Notifies that a command has been started
     * @param command the command that was started
     */
    public void commandStarted(C command, String userId);

    /**
     * Notifies that a command has finished successfully
     * @param command the command that finished
     */
    public void commandFinished(C command);

    /**
     * Notifies that a command failed to finish successfully
     * @param command the command that failed
     * @param error the cause of the failure
     */
    public void commandFailed(C command, String error);
}
