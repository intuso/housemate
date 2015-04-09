package com.intuso.housemate.api.object.server;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for commands
 */
public interface ServerFactory<C extends Server<?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<ServerData, C> {
}
