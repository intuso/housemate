package com.intuso.housemate.webserver.ui.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.webserver.ui.UIServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by tomc on 18/05/17.
 */
public class UIHandlerProvider implements Provider<ServletContextHandler> {

    private final UIServlet uiServlet;

    @Inject
    public UIHandlerProvider(UIServlet uiServlet) {
        this.uiServlet = uiServlet;
    }

    @Override
    public ServletContextHandler get() {
        // todo should return a ResourceHandler instead!
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(uiServlet), "/");
        return servletContextHandler;
    }
}
