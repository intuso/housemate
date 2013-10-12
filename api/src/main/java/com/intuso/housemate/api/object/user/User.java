package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.RemoveableObject;
import com.intuso.housemate.api.object.command.Command;

/**
 * @param <REMOVE_COMMAND> the type of the remove command
 */
public interface User<REMOVE_COMMAND extends Command<?, ?>>
        extends BaseHousemateObject<UserListener>, RemoveableObject<REMOVE_COMMAND> {
    public final static String REMOVE_COMMAND_ID = "remove";
}
