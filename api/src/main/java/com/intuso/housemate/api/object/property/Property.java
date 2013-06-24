package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <TYPE> the type of the type
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public interface Property<
            TYPE extends Type,
            SET_COMMAND extends Command<?, ?>,
            PROPERTY extends Property<TYPE, SET_COMMAND, PROPERTY>>
        extends Value<TYPE, PROPERTY> {

    public final static String SET_COMMAND_ID = "set-command";
    public final static String VALUE_PARAM = "value";

    /**
     * Sets the value of this property
     * @param value the new value
     * @param listener the listener to notify of progress
     */
    public void set(TypeInstance value, CommandListener<? super SET_COMMAND> listener);

    /**
     * Gets the set value command
     * @return the set value command
     */
    public SET_COMMAND getSetCommand();
}
