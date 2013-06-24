package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for commands
 */
public interface CommandFactory<
            R extends Resources,
            C extends Command<?, ?>>
        extends HousemateObjectFactory<R, CommandWrappable, C> {
}
