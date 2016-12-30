package com.intuso.housemate.pkg.node.v1_0.jar.ioc;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.real.impl.ioc.NodeRootModule;
import com.intuso.housemate.platform.pc.ioc.ConnectionProvider;
import com.intuso.housemate.plugin.host.internal.ioc.PluginHostModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import javax.jms.Connection;
import java.util.Set;

/**
 */
public class NodeModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;

    public NodeModule(WriteableMapPropertyRepository defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override
    protected void configure() {

        // install plugin modules
        install(new NodeRootModule(defaultProperties));
        install(new PluginHostModule());

        // bind broker and connection
        bind(Connection.class).toProvider(ConnectionProvider.class).in(Scopes.SINGLETON);
    }

    @Provides
    public ServiceManager getServiceManager(Set<Service> services) {
        return new ServiceManager(services);
    }
}
