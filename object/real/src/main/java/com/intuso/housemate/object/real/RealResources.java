package com.intuso.housemate.object.real;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.utils.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 26/03/13
 * Time: 09:17
 * To change this template use File | Settings | File Templates.
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
