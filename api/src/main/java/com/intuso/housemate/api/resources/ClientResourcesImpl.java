package com.intuso.housemate.api.resources;

import com.google.inject.Inject;
import com.intuso.housemate.api.comms.Router;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 09:07
 * To change this template use File | Settings | File Templates.
 */
public class ClientResourcesImpl extends ResourcesImpl implements ClientResources {

    private final Router router;

    @Inject
    public ClientResourcesImpl(Log log, PropertyContainer properties, Router router) {
        super(log, properties);
        this.router = router;
    }

    @Override
    public final Router getRouter() {
        return router;
    }
}
