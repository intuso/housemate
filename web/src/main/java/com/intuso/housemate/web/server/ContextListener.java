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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class ContextListener implements ServletContextListener {

    public static ClientResources RESOURCES;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // will be null if started in dev mode, in which case the broker is run separately
        try {
            if(RESOURCES == null) {
                Object resources = servletContextEvent.getServletContext().getAttribute("RESOURCES");
                if(resources != null && resources instanceof ClientResources)
                    RESOURCES = (ClientResources)resources;
                else
                    RESOURCES = new PCEnvironment(new String[0]).getResources();
            }
            RESOURCES.getRouter().connect();
            RESOURCES.getRouter().login(
                    new UsernamePassword(false, RESOURCES.getProperties().get("username"), RESOURCES.getProperties().get("password"), false));
            RESOURCES.getRouter().addObjectListener(new RootListener<RouterRootObject>() {
                @Override
                public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
                    RESOURCES.getLog().d("Router connection status: " + status);
                }

                @Override
                public void brokerInstanceChanged(RouterRootObject root) {
                    RESOURCES.getRouter().login(
                            new UsernamePassword(false, RESOURCES.getProperties().get("username"), RESOURCES.getProperties().get("password"), false)
                    );
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
