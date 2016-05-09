package com.intuso.housemate.server.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.api.bridge.ioc.ObjectAPIBridgeV1_0Module;
import com.intuso.housemate.client.real.api.bridge.v1_0.ioc.ClientRealAPIBridgeV1_0Module;
import com.intuso.housemate.client.real.impl.internal.ioc.ServerRealObjectModule;
import com.intuso.housemate.plugin.manager.internal.ioc.PluginHostModule;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(Server.SERVER_NAME, "My Server");
    }

    @Override
    protected void configure() {

        // install api bridge modules
        install(new ObjectAPIBridgeV1_0Module());
        install(new ClientRealAPIBridgeV1_0Module());

        // install plugin modules
        install(new ServerRealObjectModule());
        install(new PluginHostModule());

        // bind broker and connection
        bind(BrokerService.class).toProvider(BrokerServiceProvider.class).in(Scopes.SINGLETON);
        bind(Connection.class).toProvider(ConnectionProvider.class);

        // bind everything as singletons that should be
        bind(FactoryPluginListener.class).in(Scopes.SINGLETON);
        bind(Server.class).in(Scopes.SINGLETON);
    }
}
