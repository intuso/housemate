package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.intuso.utilities.properties.api.PropertyRepository;

import javax.jms.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/01/14
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class PCClientModule extends AbstractModule {

    private final PropertyRepository properties;

    public PCClientModule(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        install(new PCModule());
        bind(PropertyRepository.class).toInstance(properties);
        bind(Connection.class).toProvider(ConnectionProvider.class);
    }
}
