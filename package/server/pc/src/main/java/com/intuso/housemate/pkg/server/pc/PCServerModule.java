package com.intuso.housemate.pkg.server.pc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.pkg.server.pc.storage.SjoerdDBModule;
import com.intuso.housemate.platform.pc.PCModule;
import com.intuso.housemate.server.ServerModule;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class PCServerModule extends AbstractModule {

    private final PropertyContainer properties;

    public PCServerModule(PropertyContainer properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(PropertyContainer.class).toInstance(properties);
        install(new PCModule(properties, "server.log")); // log and properties provider
        install(new SjoerdDBModule(properties)); // storage impl
        install(new TransportsModule(properties)); // socket and rest servers
        install(new ServerModule(properties)); // main server module
    }
}
