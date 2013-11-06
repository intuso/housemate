package com.intuso.housemate.api.resources;

import com.intuso.housemate.api.comms.Router;

/**
 * Base resources class for clients
 */
public interface ClientResources extends Resources {

    /**
     * Gets the router the clients should connect through
     * @return the router the clients should connect through
     */
    public Router getRouter();
}
