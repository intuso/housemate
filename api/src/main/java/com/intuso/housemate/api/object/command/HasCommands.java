package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of commands
 */
public interface HasCommands<L extends List<? extends Command<?, ?>>> {

    /**
     * Gets the commands list
     * @return the commands list
     */
    public L getCommands();
}
