package com.intuso.housemate.api.resources;

import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Simple resources class
 */
public class SimpleResources implements Resources {

    private final Log log;
    private final PropertyContainer properties;

    /**
     * @param log the log instance to use
     * @param properties the properties
     */
    public SimpleResources(Log log, PropertyContainer properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    public final Log getLog() {
        return log;
    }

    @Override
    public final PropertyContainer getProperties() {
        return properties;
    }
}
