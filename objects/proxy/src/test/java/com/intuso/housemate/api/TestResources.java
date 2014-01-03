package com.intuso.housemate.api;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestResources implements Resources {

    private final Log log;
    private final PropertyContainer properties;

    public TestResources(Log log, PropertyContainer properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public PropertyContainer getProperties() {
        return properties;
    }
}
