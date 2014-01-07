package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.comms.transport.socket.client.SocketClientModule;
import com.intuso.housemate.platform.pc.PCModule;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 */
public class ContextListener implements ServletContextListener {

    public static Injector INJECTOR;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // will be null if started in dev mode, in which case the server is run separately
        if(INJECTOR == null) {
            Object injector = servletContextEvent.getServletContext().getAttribute("INJECTOR");
            if(injector != null && injector instanceof Injector)
                INJECTOR = (Injector)injector;
            else {
                PropertyContainer properties = new PropertyContainer();
                INJECTOR = Guice.createInjector(new PCModule(properties, "web-server.log"), new SocketClientModule(properties));
            }
        }
        final PropertyContainer properties = INJECTOR.getInstance(PropertyContainer.class);
        final Router router = INJECTOR.getInstance(Router.class);
        final Log log = INJECTOR.getInstance(Log.class);
        router.connect();
        router.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
                log.d("Router connection status: " + status);
                if (status == ConnectionStatus.Unauthenticated)
                    router.login(
                            new UsernamePassword(properties.get("username"), properties.get("password"), false));
            }

            @Override
            public void newServerInstance(RouterRootObject root) {
                log.d("Server instance changed");
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        INJECTOR.getInstance(Router.class).disconnect();
    }
}
