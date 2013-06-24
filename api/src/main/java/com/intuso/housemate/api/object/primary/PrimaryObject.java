package com.intuso.housemate.api.object.primary;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <CONNECTED_VALUE> the type of the connected value
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the primary object's listener
 */
public interface PrimaryObject<
            REMOVE_COMMAND extends Command<?, ?>,
            START_STOP_COMMAND extends Command<?, ?>,
            CONNECTED_VALUE extends Value<?, ?>,
            RUNNING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            PRIMARY_OBJECT extends PrimaryObject<REMOVE_COMMAND, START_STOP_COMMAND, CONNECTED_VALUE, RUNNING_VALUE, ERROR_VALUE, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends BaseObject<LISTENER> {

    public final static String REMOVE_COMMAND_ID = "remove";
    public final static String CONNECTED_VALUE_ID = "connected";
    public final static String RUNNING_VALUE_ID = "running";
    public final static String START_COMMAND_ID = "start";
    public final static String STOP_COMMAND_ID = "stop";
    public final static String ERROR_VALUE_ID = "error";

    /**
     * Gets the remove command
     * @return the remove command
     */
    public REMOVE_COMMAND getRemoveCommand();

    /**
     * Gets the connected value object
     * @return the connected value
     */
    public CONNECTED_VALUE getConnectedValue();

    /**
     * Gets the connected value
     * @return the connected value
     */
    public boolean isConnected();

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

    /**
     * Gets the error value object
     * @return the error value object
     */
    public ERROR_VALUE getErrorValue();

    /**
     * Gets the error value
     * @return the error value
     */
    public String getError();

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
}
