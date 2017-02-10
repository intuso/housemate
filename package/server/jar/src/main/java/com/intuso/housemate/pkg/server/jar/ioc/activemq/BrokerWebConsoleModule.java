package com.intuso.housemate.pkg.server.jar.ioc.activemq;

import com.google.inject.AbstractModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by tomc on 21/04/16.
 */
public class BrokerWebConsoleModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        BrokerWebAppContextProvider.configureDefaults(defaultProperties);
    }

    @Override
    protected void configure() {
        bind(WebAppContext.class).annotatedWith(Broker.class).toProvider(BrokerWebAppContextProvider.class);
    }
}
