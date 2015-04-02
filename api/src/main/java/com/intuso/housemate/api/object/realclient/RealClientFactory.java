package com.intuso.housemate.api.object.realclient;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for commands
 */
public interface RealClientFactory<C extends RealClient<?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<RealClientData, C> {
}
