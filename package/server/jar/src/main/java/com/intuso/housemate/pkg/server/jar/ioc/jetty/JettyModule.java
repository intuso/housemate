package com.intuso.housemate.pkg.server.jar.ioc.jetty;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * Created by tomc on 27/10/14.
 */
public class JettyModule extends AbstractModule {

    public JettyModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set("web.ports", "http");
        defaultProperties.set("web.ports.http.type", "http");
        defaultProperties.set("web.ports.http.port", "46874");
        defaultProperties.set("web.ports.http.host", "0.0.0.0");
    }

    @Override
    protected void configure() {
        bind(ThreadPool.class).toProvider(ThreadPoolProvider.class);
        bind(Handler.class).to(ContextHandlerCollection.class);
        bind(ContextHandlerCollection.class).in(Scopes.SINGLETON);
        bind(Server.class).toProvider(JettyServerProvider.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(JettyServerService.class);
        bind(JettyServerService.class).in(Scopes.SINGLETON);
    }
}
