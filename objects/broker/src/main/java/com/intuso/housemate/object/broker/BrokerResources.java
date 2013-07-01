package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.RealResources;

/**
 * @param <ROOT> the type of the root object
 */
public interface BrokerResources<ROOT> extends Resources {

    /**
     * Gets the resources for real broker objects
     * @return the resources for real broker objects
     */
    BrokerRealResources getBrokerRealResources();

    /**
     * Gets the resources for real client objects
     * @return the resources for real client objects
     */
    RealResources getRealResources();

    /**
     * Gets the lifecycle handler
     * @return the lifecycle handler
     */
    LifecycleHandler getLifecycleHandler();

    /**
     * Gets the root object for this resources
     * @return the root object for this resources
     */
    ROOT getRoot();
}
