package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for commands
 */
public interface CommandFactory<
            C extends Command<?, ?>>
        extends HousemateObjectFactory<CommandData, C> {
}
