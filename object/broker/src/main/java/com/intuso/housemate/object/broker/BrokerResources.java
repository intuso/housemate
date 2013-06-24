package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.RealResources;

/**
 */
public interface BrokerResources<R> extends Resources {
    BrokerRealResources getBrokerRealResources();
    RealResources getRealResources();
    LifecycleHandler getLifecycleHandler();
    R getRoot();
}
