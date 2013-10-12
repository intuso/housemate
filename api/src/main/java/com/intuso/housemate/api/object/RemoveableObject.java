package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.command.Command;

public interface RemoveableObject<REMOVE_COMMAND extends Command<?, ?>> {

    /**
     * Gets the remove command
     * @return the remove command
     */
    public REMOVE_COMMAND getRemoveCommand();
}
