package com.intuso.housemate.api.resources;

import com.intuso.utilities.log.Log;

import java.util.Map;

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
    public Map<String, String> getProperties();
}
