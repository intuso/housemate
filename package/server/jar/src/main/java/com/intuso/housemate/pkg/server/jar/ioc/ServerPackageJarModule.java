package com.intuso.housemate.pkg.server.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.pkg.server.jar.ioc.activemq.BrokerWebConsoleModule;
import com.intuso.housemate.pkg.server.jar.web.ServerFilter;
import com.intuso.housemate.platform.pc.ioc.PCModule;
import com.intuso.housemate.server.ioc.ServerModule;
import com.intuso.housemate.webserver.database.mongo.ioc.MongoDatabaseModule;
import com.intuso.housemate.webserver.ioc.HousemateWebServerModule;
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

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        PCModule.configureDefaults(defaultProperties);
        ConfigsProvider.configureDefaults(defaultProperties);
        ServerModule.configureDefaults(defaultProperties);
        HousemateWebServerModule.configureDefaults(defaultProperties);
        MongoDatabaseModule.configureDefaults(defaultProperties);
        BrokerWebConsoleModule.configureDefaults(defaultProperties);
    }

    private final PropertyRepository properties;

    public ServerPackageJarModule(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(PropertyRepository.class).toInstance(properties);
        // log and properties provider
        install(new PCModule());
        // data serialisers
        install(new SerialisersModule());
        // main server module
        install(new ServerModule());
        // web server
        install(new HousemateWebServerModule(4601, ServerFilter.class));
        // mongo database
        install(new MongoDatabaseModule());
        // active mq broker web console setup
        install(new BrokerWebConsoleModule());
    }
}
