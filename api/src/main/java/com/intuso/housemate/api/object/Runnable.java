package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * Classes implementing this are runnable objects
 * @param <START_STOP_COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the value
 */
public interface Runnable<START_STOP_COMMAND extends Command<?, ?, ?>, RUNNING_VALUE extends Value<?, ?>> {

    public final static String RUNNING_ID = "running";
    public final static String START_ID = "start";
    public final static String STOP_ID = "stop";

    /**
     * Gets the start command
     * @return the start command
     */
    public START_STOP_COMMAND getStartCommand();

    /**
     * Gets the stop command
     * @return the stop command
     */
    public START_STOP_COMMAND getStopCommand();

    /**
     * Gets the running value object
     * @return the running value object
     */
    public RUNNING_VALUE getRunningValue();

    /**
     * Gets the running value
     * @return the running value
     */
    public boolean isRunning();
}