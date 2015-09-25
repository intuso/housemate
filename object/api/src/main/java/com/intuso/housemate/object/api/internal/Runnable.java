package com.intuso.housemate.object.api.internal;

/**
 * Classes implementing this are runnable objects
 * @param <START_STOP_COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the value
 */
public interface Runnable<START_STOP_COMMAND extends Command<?, ?, ?, ?>, RUNNING_VALUE extends Value<?, ?>> {

    /**
     * Gets the start command
     * @return the start command
     */
    START_STOP_COMMAND getStartCommand();

    /**
     * Gets the stop command
     * @return the stop command
     */
    START_STOP_COMMAND getStopCommand();

    /**
     * Gets the running value object
     * @return the running value object
     */
    RUNNING_VALUE getRunningValue();

    interface Listener<RUNNABLE extends Runnable> {

        /**
         * Notifies that the primary object's running status has changed
         * @param runnable the primary object that was started/stopped
         * @param running whether it's running or not
         */
        void running(RUNNABLE runnable, boolean running);
    }
}