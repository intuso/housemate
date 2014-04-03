package com.intuso.housemate.comms.transport.rest;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.comms.transport.rest.resources.ContextualResource;
import com.intuso.housemate.comms.transport.rest.resources.GenericResource;
import com.intuso.housemate.plugin.api.ExternalClientRouter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class RestServer extends ExternalClientRouter {

    public final static String PORT = "rest.server.port";

    private final Injector injector;
    private final Server server;

    @Inject
    public RestServer(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Injector injector, Router router) {
        super(log, listenersFactory, properties, router);

        this.injector = injector;

        String port = properties.get(PORT);

        log.d("Creating REST server on port " + port);
        server = new Server(Integer.parseInt(port));

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("");
        // adds Jersey Servlet with a customized ResourceConfig
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig())), "/*");
        server.setHandler(handler);
    }

    public void _start() throws HousemateException {
        try {
            server.start();
        } catch (Exception e) {
            getLog().e("Could not start the server", e);
            throw new HousemateException("Could not start the REST server", e);
        }
    }

    public void _stop() {
        try {
            server.stop();
        } catch(Exception e) {
            getLog().e("Could not stop the REST server", e);
        }
    }

    private ResourceConfig resourceConfig() {
        return new ResourceConfig()
                .register(new GenericResource(this))
                .register(new ContextualResource(injector));
    }
}
