package com.intuso.housemate.pkg.server.jar.ioc.jetty;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.pkg.server.jar.ioc.activemq.Broker;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by Ravn Systems.
 * User: rathins
 * Date: 22/05/13
 * Time: 16:24
 */
public class JettyServerProvider implements Provider<Server> {

    private final ThreadPool threadPool;
    private final Handler handler;

    @Inject
    public JettyServerProvider(ThreadPool threadPool, Handler handler,
                               @Broker WebAppContext brokerWebAppContext /* Dummy dependency, forces it to be created and therefore hosted */) {
        this.threadPool = threadPool;
        this.handler = handler;
    }

    @Override
    public Server get() {
        Server server = new Server(threadPool);
        server.setHandler(handler);
        server.setStopAtShutdown(true);
        return server;
    }
}