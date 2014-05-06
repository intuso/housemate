package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.command.Command;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface RemoveableObject<REMOVE_COMMAND extends Command<?, ?, ?>> {

    public final static String REMOVE_ID = "remove";

    /**
     * Gets the remove command
     * @return the remove command
     */
    public REMOVE_COMMAND getRemoveCommand();
}
