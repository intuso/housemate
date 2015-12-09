package com.intuso.housemate.comms.transport.rest.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.SimpleProxyModule;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.transport.rest.server.v1_0.json.GsonJsonProvider;
import com.intuso.housemate.comms.transport.rest.server.v1_0.resources.ContextualResource;
import com.intuso.housemate.comms.transport.rest.server.v1_0.resources.GenericResource;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.plugin.api.internal.ExternalClientRouter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class RestServer extends ExternalClientRouter<RestServer> {

    public final static String PORT = "rest.server.port";

    private final Injector injector;
    private final Server server;

    @Inject
    public RestServer(Logger log, ListenersFactory listenersFactory, PropertyRepository properties, Injector injector, Router<?> router) {
        super(log, listenersFactory, router);

        this.injector = injector.createChildInjector(new SimpleProxyModule());

        String port = properties.get(PORT);

        log.debug("Creating REST server on port " + port);
        server = new Server(Integer.parseInt(port));

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("");
        // adds Jersey Servlet with a customized ResourceConfig
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig())), "/*");
        server.setHandler(handler);
    }

    public void _start() {
        try {
            server.start();
        } catch (Exception e) {
            getLogger().error("Could not start the server", e);
            throw new HousemateCommsException("Could not start the REST server", e);
        }
    }

    public void _stop() {
        try {
            server.stop();
        } catch(Exception e) {
            getLogger().error("Could not stop the REST server", e);
        }
    }

    private ResourceConfig resourceConfig() {
        return new ResourceConfig()
                .register(GsonJsonProvider.class)
                .register(injector.getInstance(GenericResource.class))
                .register(injector.getInstance(ContextualResource.class));
    }
}
