package com.intuso.housemate.broker.object;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealRootObject;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
@Singleton
public class BrokerRealResourcesImpl extends BrokerResourcesImpl<BrokerRealRootObject> implements BrokerRealResources {

    private final RealResources realResources;

    @Inject
    public BrokerRealResourcesImpl(Log log, Map<String, String> properties, Injector injector, RealResources realResources) {
        super(log, properties, injector);
        this.realResources = realResources;
    }

    @Override
    public RealResources getRealResources() {
        return realResources;
    }
}
