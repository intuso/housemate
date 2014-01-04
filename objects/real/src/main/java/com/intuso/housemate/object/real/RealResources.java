package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Resources container for real objects
 */
public class RealResources implements ClientResources {

    private final Log log;
    private final PropertyContainer properties;
    private final Router router;

    /**
     * @param log the log instance to use
     * @param properties the properties to use
     * @param router the router to connect through
     */
    @Inject
    public RealResources(Log log, PropertyContainer properties, Router router) {
        this.log = log;
        this.properties = properties;
        this.router = router;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public PropertyContainer getProperties() {
        return properties;
    }

    @Override
    public Router getRouter() {
        return router;
    }
}
