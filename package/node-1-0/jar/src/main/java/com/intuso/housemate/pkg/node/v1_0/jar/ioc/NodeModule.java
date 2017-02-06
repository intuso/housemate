package com.intuso.housemate.pkg.node.v1_0.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ioc.ProxyWrapperModule;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.SimpleProxyServerModule;
import com.intuso.housemate.client.v1_0.real.impl.ioc.NodeRootModule;
import com.intuso.housemate.plugin.host.internal.ioc.PluginHostModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 */
public class NodeModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;

    public NodeModule(WriteableMapPropertyRepository defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override
    protected void configure() {
        // install real object stuff
        install(new NodeRootModule(defaultProperties));

        // install proxy object stuff
        install(new SimpleProxyServerModule());

        // install proxy wrapper modules
        install(new ProxyWrapperModule());

        // install plugin modules
        install(new PluginHostModule());
    }
}
