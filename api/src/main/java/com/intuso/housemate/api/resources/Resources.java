package com.intuso.housemate.api.resources;

import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Base interface for all resources
 */
public interface Resources {

    /**
     * Gets the log instance to use
     * @return the log instance to use
     */
    public Log getLog();

    /**
     * Gets the properties
     * @return the properties
     */
    public PropertyContainer getProperties();
}
