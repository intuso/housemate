package com.intuso.housemate.server.object.general;

import com.google.inject.Injector;
import com.intuso.housemate.object.server.ServerResources;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
public abstract class ServerResourcesImpl<R> implements ServerResources<R> {

    private final Log log;
    private final PropertyContainer properties;
    private final Injector injector;
    private R root;

    public ServerResourcesImpl(Log log, PropertyContainer properties, Injector injector) {
        this.log = log;
        this.properties = properties;
        this.injector = injector;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public PropertyContainer getProperties() {
        return properties;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public R getRoot() {
        return root;
    }

    public void setRoot(R root) {
        this.root = root;
    }
}
