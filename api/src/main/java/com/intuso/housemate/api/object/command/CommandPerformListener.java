package com.intuso.housemate.api.object.command;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/01/14
 * Time: 08:27
 * To change this template use File | Settings | File Templates.
 */
public interface CommandPerformListener<C extends Command<?, ?, ?>> {

    /**
     * Notifies that a command has been started
     * @param command the command that was started
     */
    public void commandStarted(C command);

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
