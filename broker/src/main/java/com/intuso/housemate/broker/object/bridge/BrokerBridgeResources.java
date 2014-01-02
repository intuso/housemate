package com.intuso.housemate.broker.object.bridge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
@Singleton
public class BrokerBridgeResources extends BrokerResourcesImpl<RootObjectBridge> {

    @Inject
    public BrokerBridgeResources(Log log, Map<String, String> properties, Injector injector) {
        super(log, properties, injector);
    }
}
