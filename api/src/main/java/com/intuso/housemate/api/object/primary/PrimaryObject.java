package com.intuso.housemate.api.object.primary;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.RemoveableObject;
import com.intuso.housemate.api.object.RunnableObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the primary object's listener
 */
public interface PrimaryObject<
            REMOVE_COMMAND extends Command<?, ?>,
            START_STOP_COMMAND extends Command<?, ?>,
            RUNNING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            PRIMARY_OBJECT extends PrimaryObject<REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends
            BaseHousemateObject<LISTENER>,
            RunnableObject<START_STOP_COMMAND, RUNNING_VALUE>,
            RemoveableObject<REMOVE_COMMAND> {

    public final static String REMOVE_ID = "remove";
    public final static String CONNECTED_ID = "connected";
    public final static String RUNNING_ID = "running";
    public final static String START_ID = "start";
    public final static String STOP_ID = "stop";
    public final static String ERROR_ID = "error";

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
}
