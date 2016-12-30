package com.intuso.housemate.pkg.server.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.pkg.server.jar.ioc.activemq.BrokerWebConsoleModule;
import com.intuso.housemate.pkg.server.jar.ioc.jetty.JettyModule;
import com.intuso.housemate.platform.pc.ioc.PCModule;
import com.intuso.housemate.server.ioc.ServerModule;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class ServerPackageJarModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;
    private final PropertyRepository properties;

    public ServerPackageJarModule(WriteableMapPropertyRepository defaultProperties, PropertyRepository properties) {
        this.defaultProperties = defaultProperties;
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(PropertyRepository.class).toInstance(properties);
        install(new PCModule()); // log and properties provider
        install(new SerialisersModule());
        install(new ServerModule(defaultProperties)); // main server module
        install(new JettyModule(defaultProperties)); // web server
        install(new BrokerWebConsoleModule(defaultProperties)); // active mq broker web console setup
    }
}
