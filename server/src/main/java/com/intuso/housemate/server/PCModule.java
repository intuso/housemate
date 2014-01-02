package com.intuso.housemate.server;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/11/13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    private final Log log;
    private final Map<String, String> properties;

    public PCModule(Log log, Map<String, String> properties) {
        this.log = log;
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(Log.class).toInstance(log);
        bind(new TypeLiteral<Map<String, String>>() {}).toInstance(properties);
    }
}
