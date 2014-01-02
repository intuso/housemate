package com.intuso.housemate.object.broker;

import com.google.inject.Injector;
import com.intuso.housemate.api.resources.Resources;

/**
 * @param <ROOT> the type of the root object
 */
public interface BrokerResources<ROOT> extends Resources {

    /**
     * Gets the root object for this resources
     * @return the root object for this resources
     */
    ROOT getRoot();

    Injector getInjector();
}
