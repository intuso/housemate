package com.intuso.housemate.api.resources;

import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Simple resources class
 */
public class SimpleResources implements Resources {

    private final Log log;
    private final Map<String, String> properties;

    /**
     * @param log the log instance to use
     * @param properties the properties
     */
    public SimpleResources(Log log, Map<String, String> properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    public final Log getLog() {
        return log;
    }

    @Override
    public final Map<String, String> getProperties() {
        return properties;
    }
}
