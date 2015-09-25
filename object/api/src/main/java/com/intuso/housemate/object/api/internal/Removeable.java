package com.intuso.housemate.object.api.internal;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface Removeable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> {

    /**
     * Gets the remove command
     * @return the remove command
     */
    REMOVE_COMMAND getRemoveCommand();
}
