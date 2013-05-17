package com.intuso.housemate.web.server;

import com.intuso.housemate.broker.App;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.authentication.UsernamePassword;
import com.intuso.housemate.core.resources.ClientResources;
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
        try {
            // will be null if started in dev mode, in which case the broker is run separately
            if(com.intuso.jetty.embedded.App.ARGS == null) {
                RESOURCES = new PCEnvironment(new String[0]).getResources();
            } else {
                RESOURCES = App.start(new String[0]).getGeneralResources().getClientResources();
            }
            RESOURCES.getRouter().connect(new UsernamePassword(false, "admin", "admin", false), null);
        } catch(HousemateException e) {
            System.err.println("Failed to start broker");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        RESOURCES.getRouter().disconnect();
    }
}
