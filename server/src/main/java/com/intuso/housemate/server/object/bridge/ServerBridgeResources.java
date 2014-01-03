package com.intuso.housemate.server.object.bridge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.server.object.general.ServerResourcesImpl;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
@Singleton
public class ServerBridgeResources extends ServerResourcesImpl<RootObjectBridge> {

    @Inject
    public ServerBridgeResources(Log log, PropertyContainer properties, Injector injector) {
        super(log, properties, injector);
    }
}
