package com.intuso.housemate.server.ioc;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.api.bridge.ioc.ObjectApiBridgeModule;
import com.intuso.housemate.client.proxy.api.bridge.ioc.ProxyBridgeModule;
import com.intuso.housemate.client.real.api.bridge.ioc.RealApiBridgeModule;
import com.intuso.housemate.client.real.impl.internal.ioc.ServerRootModule;
import com.intuso.housemate.plugin.host.internal.ioc.PluginHostModule;
import com.intuso.housemate.server.ServerService;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;
import java.util.Set;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(ServerService.SERVER_NAME, "My Server");
    }

    @Override
    protected void configure() {

        // install api bridge modules
        install(new ObjectApiBridgeModule());
        install(new RealApiBridgeModule());
        install(new ProxyBridgeModule());

        // install plugin modules
        install(new ServerRootModule());
        install(new PluginHostModule());

        // bind broker and connection
        bind(BrokerService.class).toProvider(BrokerServiceProvider.class).in(Scopes.SINGLETON);
        bind(Connection.class).toProvider(ConnectionProvider.class);

        // bind everything as singletons that should be
        bind(FactoryPluginListener.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(ServerService.class).in(Scopes.SINGLETON);
    }

    @Provides
    public ServiceManager getServiceManager(Set<Service> services) {
        return new ServiceManager(services);
    }
}
