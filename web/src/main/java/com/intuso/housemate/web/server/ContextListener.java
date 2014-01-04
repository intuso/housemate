package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.ClientResourcesImpl;
import com.intuso.housemate.comms.transport.socket.client.SocketClientModule;
import com.intuso.housemate.platform.pc.PCModule;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 */
public class ContextListener implements ServletContextListener {

    public static ClientResources RESOURCES;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // will be null if started in dev mode, in which case the server is run separately
        if(RESOURCES == null) {
            Object resources = servletContextEvent.getServletContext().getAttribute("RESOURCES");
            if(resources != null && resources instanceof ClientResources)
                RESOURCES = (ClientResources)resources;
            else {
                Injector injector = Guice.createInjector(new PCModule(new String[0]), new SocketClientModule());
                RESOURCES = injector.getInstance(ClientResourcesImpl.class);
            }
        }
        RESOURCES.getRouter().connect();
        RESOURCES.getRouter().addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
                RESOURCES.getLog().d("Router connection status: " + status);
                if(status == ConnectionStatus.Unauthenticated)
                    RESOURCES.getRouter().login(
                            new UsernamePassword(RESOURCES.getProperties().get("username"), RESOURCES.getProperties().get("password"), false));
            }

            @Override
            public void newServerInstance(RouterRootObject root) {
                RESOURCES.getLog().d("Server instance changed");
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        RESOURCES.getRouter().disconnect();
    }
}
