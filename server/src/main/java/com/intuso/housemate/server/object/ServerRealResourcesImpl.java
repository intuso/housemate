package com.intuso.housemate.server.object;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.server.object.general.ServerResourcesImpl;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
@Singleton
public class ServerRealResourcesImpl extends ServerResourcesImpl<ServerRealRootObject> implements ServerRealResources {

    private final RealResources realResources;

    @Inject
    public ServerRealResourcesImpl(Log log, PropertyContainer properties, Injector injector, RealResources realResources) {
        super(log, properties, injector);
        this.realResources = realResources;
    }

    @Override
    public RealResources getRealResources() {
        return realResources;
    }
}
