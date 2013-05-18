package com.intuso.housemate.web.server;

import com.intuso.housemate.core.authentication.UsernamePassword;
import com.intuso.housemate.core.resources.ClientResources;

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
        RESOURCES.getRouter().connect(new UsernamePassword(false, "admin", "admin", false), null);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        RESOURCES.getRouter().disconnect();
    }
}
