package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.comms.serialiser.javabin.JavabinSerialiserClientModule;
import com.intuso.housemate.comms.transport.socket.client.SocketClientModule;
import com.intuso.housemate.persistence.flatfile.FlatFilePersistenceModule;
import com.intuso.housemate.platform.pc.PCClientModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import javax.servlet.ServletContextEvent;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
public class ContextListener extends GuiceServletContextListener {

    private final static ApplicationDetails APPLICATION_DETAILS = new ApplicationDetails(ContextListener.class.getPackage().getName(), "Housemate Web Interface Server", "Housemate Web Interface Server");

    public static Injector INJECTOR;

    @Override
    protected Injector getInjector() {
        return INJECTOR;
    }

    private void setINJECTOR(ServletContextEvent servletContextEvent) {
        Object injector = servletContextEvent.getServletContext().getAttribute(Injector.class.getName());
        if(injector != null && injector instanceof Injector)
            INJECTOR = (Injector)injector;
        else {
            ListenersFactory listenersFactory = new ListenersFactory() {
                @Override
                public <LISTENER extends Listener> Listeners<LISTENER> create() {
                    return new Listeners<LISTENER>(new CopyOnWriteArrayList<LISTENER>());
                }
            };
            WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
            PropertyRepository properties = Properties.create(listenersFactory, defaultProperties, new String[]{"-log.stdout.level", "DEBUG"});
            INJECTOR = Guice.createInjector(
                    new PCClientModule(defaultProperties, properties, "web-server.log"),
                    new JavabinSerialiserClientModule(),
                    new SocketClientModule(defaultProperties),
                    new FlatFilePersistenceModule(defaultProperties));
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        setINJECTOR(servletContextEvent);

        super.contextInitialized(servletContextEvent);

        // will be null if started in dev mode, in which case the server is run separately
        final PropertyRepository properties = INJECTOR.getInstance(PropertyRepository.class);
        final Router router = INJECTOR.getInstance(Router.class);
        final Log log = INJECTOR.getInstance(Log.class);
        router.addObjectListener(new RootListener<RouterRoot>() {

            @Override
            public void statusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
                log.d("Server connection status: " + serverConnectionStatus);
                log.d("Application status: " + applicationStatus);
                log.d("Application instance status: " + applicationInstanceStatus);
                if (serverConnectionStatus == ServerConnectionStatus.ConnectedToServer
                        && applicationInstanceStatus == ApplicationInstanceStatus.Unregistered)
                    router.register(APPLICATION_DETAILS);
            }

            @Override
            public void newApplicationInstance(RouterRoot root, String instanceId) {
                log.d("Application instance changed");
            }

            @Override
            public void newServerInstance(RouterRoot root, String serverId) {
                log.d("Server instance changed");
            }
        });
        router.connect();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        INJECTOR.getInstance(Router.class).disconnect();
        super.contextDestroyed(servletContextEvent);
    }
}
