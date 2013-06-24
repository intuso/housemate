package com.intuso.housemate.api;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.log.Log;
import org.junit.Ignore;

import java.util.Map;

/**
 */
@Ignore
public class TestResources implements Resources {

    private final Log log;
    private final Map<String, String> properties;

    public TestResources(Log log, Map<String, String> properties) {
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
