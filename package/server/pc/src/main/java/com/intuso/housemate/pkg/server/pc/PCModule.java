package com.intuso.housemate.pkg.server.pc;

import com.google.inject.AbstractModule;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/11/13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    private final Log log;
    private final PropertyContainer properties;

    public PCModule(Log log, PropertyContainer properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(Log.class).toInstance(log);
        bind(PropertyContainer.class).toInstance(properties);
    }
}
