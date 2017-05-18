package com.intuso.housemate.pkg.server.jar.ioc.activemq;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * Created by tomc on 21/04/16.
 */
public class BrokerWebConsoleModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        BrokerHandlerProvider.configureDefaults(defaultProperties);
    }

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), ContextHandler.class).addBinding().toProvider(BrokerHandlerProvider.class);
    }
}
