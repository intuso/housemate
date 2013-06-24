package com.intuso.housemate.object.real;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class RealResources implements ClientResources {

    private final Log log;
    private final Map<String, String> properties;
    private final Router router;

    public RealResources(Log log, Map<String, String> properties, Router router) {
        this.log = log;
        this.properties = properties;
        this.router = router;
    }

    public Log getLog() {
        return log;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Router getRouter() {
        return router;
    }
}
