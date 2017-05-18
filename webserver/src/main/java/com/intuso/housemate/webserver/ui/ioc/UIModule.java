package com.intuso.housemate.webserver.ui.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * Created by tomc on 08/03/17.
 */
public class UIModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        UIHandlerProvider.configureDefaults(defaultProperties);
    }

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), ContextHandler.class).addBinding().toProvider(UIHandlerProvider.class);
    }
}
