package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.command.Command;

/**
 * @param <REMOVE_COMMAND> the type of the remove command
 */
public interface User<REMOVE_COMMAND extends Command<?, ?>>
        extends BaseObject<UserListener> {

    public final static String REMOVE_COMMAND_ID = "remove";

    /**
     * Gets the remove command
     * @return the remove command
     */
    public REMOVE_COMMAND getRemoveCommand();
}
