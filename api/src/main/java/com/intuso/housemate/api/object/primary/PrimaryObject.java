package com.intuso.housemate.api.object.primary;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <RENAME_COMMAND> the type of the command for renaming the object
 * @param <REMOVE_COMMAND> the type of the command for removing the object
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the primary object's listener
 */
public interface PrimaryObject<
            RENAME_COMMAND extends Command<?, ?, ?>,
            REMOVE_COMMAND extends Command<?, ?, ?>,
            START_STOP_COMMAND extends Command<?, ?, ?>,
            RUNNING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            PRIMARY_OBJECT extends PrimaryObject<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends
            BaseHousemateObject<LISTENER>,
            Renameable<RENAME_COMMAND>,
            Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
            Removeable<REMOVE_COMMAND> {

    public final static String CONNECTED_ID = "connected";
    public final static String NEW_NAME = "new-name";
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
