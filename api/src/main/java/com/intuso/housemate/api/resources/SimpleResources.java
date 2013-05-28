package com.intuso.housemate.api.resources;

import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/04/13
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
public class SimpleResources implements Resources {

    private final Log log;
    private final Map<String, String> properties;

    public SimpleResources(Log log, Map<String, String> properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }
}
