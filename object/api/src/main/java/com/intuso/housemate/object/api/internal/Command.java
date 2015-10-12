package com.intuso.housemate.object.api.internal;

/**
 * @param <ENABLED_VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETERS> the type of the propeties list
 * @param <COMMAND> the type of the command
 */
public interface Command<
            DATA_TYPE,
            ENABLED_VALUE extends Value<?, ?>,
            PARAMETERS extends List<? extends Parameter<?>>,
            COMMAND extends Command<?, ?, ?, ?>>
        extends
            BaseHousemateObject<Command.Listener<? super COMMAND>>,
        Parameter.Container<PARAMETERS> {

    ENABLED_VALUE getEnabledValue();

    /**
     * Performs the command
     * @param value the values of the parameters
     * @param listener the listener to call about progress of the command
     */
    void perform(DATA_TYPE value, PerformListener<? super COMMAND> listener);

    /**
     *
     * Listener interface for command
     */
    interface Listener<COMMAND extends Command<?, ?, ?, ?>> extends ObjectListener {

        void commandEnabled(COMMAND command, boolean enabled);

        /**
         * Notifies that a command has been started
         * @param command the command that was started
         * @param userId the id of the user who started the command
         */
        void commandStarted(COMMAND command, String userId);

        /**
         * Notifies that a command has finished successfully
         * @param command the command that finished
         */
        void commandFinished(COMMAND command);

        /**
         * Notifies that a command failed to finish successfully
         * @param command the command that failed
         * @param error the cause of the failure
         */
        void commandFailed(COMMAND command, String error);
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 24/01/14
     * Time: 08:27
     * To change this template use File | Settings | File Templates.
     */
    interface PerformListener<COMMAND extends Command<?, ?, ?, ?>> {

        /**
         * Notifies that a command has been started
         * @param command the command that was started
         */
        void commandStarted(COMMAND command);

        /**
         * Notifies that a command has finished successfully
         * @param command the command that finished
         */
        void commandFinished(COMMAND command);

        /**
         * Notifies that a command failed to finish successfully
         * @param command the command that failed
         * @param error the cause of the failure
         */
        void commandFailed(COMMAND command, String error);
    }

    /**
     *
     * Interface to show that the implementing object has a list of commands
     */
    interface Container<COMMANDS extends List<? extends Command<?, ?, ?, ?>>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        COMMANDS getCommands();
    }
}
