package com.intuso.housemate.web.server;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.platform.pc.PCEnvironment;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 */
public class ContextListener implements ServletContextListener {

    public static ClientResources RESOURCES;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // will be null if started in dev mode, in which case the server is run separately
        try {
            if(RESOURCES == null) {
                Object resources = servletContextEvent.getServletContext().getAttribute("RESOURCES");
                if(resources != null && resources instanceof ClientResources)
                    RESOURCES = (ClientResources)resources;
                else
                    RESOURCES = new PCEnvironment(new String[0]).getResources();
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
        } catch(HousemateException e) {
            System.err.println("Failed to start Housemate platform");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        RESOURCES.getRouter().disconnect();
    }
}
