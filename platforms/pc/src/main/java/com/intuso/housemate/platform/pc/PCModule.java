package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    private final PropertyContainer properties;
    private final String defaultLogName;

    public PCModule(PropertyContainer properties, String defaultLogName) {
        this.properties = properties;
        this.defaultLogName = defaultLogName;
    }

    @Override
    protected void configure() {
        install(new PCLogModule(properties, defaultLogName));
        install(new PCRegexMatcherModule());
    }
}
