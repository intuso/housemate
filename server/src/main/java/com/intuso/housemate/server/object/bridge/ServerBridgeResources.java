package com.intuso.housemate.server.object.bridge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.server.object.general.ServerResourcesImpl;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
@Singleton
public class ServerBridgeResources extends ServerResourcesImpl<RootObjectBridge> {

    @Inject
    public ServerBridgeResources(Log log, Map<String, String> properties, Injector injector) {
        super(log, properties, injector);
    }
}
