package com.intuso.housemate.webserver.ui.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.webserver.ui.UIServlet;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * Created by tomc on 08/03/17.
 */
public class UIModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        UIServlet.configureDefaults(defaultProperties);
    }

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), ContextHandler.class).addBinding().toProvider(UIHandlerProvider.class);
        bind(UIServlet.class).in(Scopes.SINGLETON);
    }
}
