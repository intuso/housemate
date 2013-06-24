package com.intuso.housemate.api.resources;

import com.intuso.housemate.api.comms.Router;

public interface ClientResources extends Resources {

    /**
     * Gets the router the clients should connect through
     * @returnthe router the clients should connect through
     */
    public Router getRouter();
}
