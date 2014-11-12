package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.comms.serialiser.json.JsonSerialiserClientModule;
import com.intuso.housemate.comms.transport.socket.client.SocketClientModule;
import com.intuso.housemate.persistence.api.Persistence;
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
import java.util.Set;
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
                    new JsonSerialiserClientModule(),
                    new SocketClientModule(defaultProperties),
                    new FlatFilePersistenceModule(defaultProperties));
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        setINJECTOR(servletContextEvent);

        super.contextInitialized(servletContextEvent);

        final Log log = INJECTOR.getInstance(Log.class);
        final Router router = INJECTOR.getInstance(Router.class);
        Persistence persistence = INJECTOR.getInstance(Persistence.class);

        checkDefaultUser(log, persistence);

        // will be null if started in dev mode, in which case the server is run separately
        router.addObjectListener(new RootListener<RouterRoot>() {

            private boolean needsRegistering = true;

            @Override
            public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
                log.d("Server connection status: " + serverConnectionStatus);
                if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently) {
                    needsRegistering = true;
                    router.connect();
                } else if((serverConnectionStatus == ServerConnectionStatus.ConnectedToServer || serverConnectionStatus == ServerConnectionStatus.DisconnectedTemporarily) && needsRegistering) {
                    needsRegistering = false;
                    router.register(APPLICATION_DETAILS);
                }
            }

            @Override
            public void applicationStatusChanged(RouterRoot root, ApplicationStatus applicationStatus) {
                log.d("Application status: " + applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                log.d("Application instance status: " + applicationInstanceStatus);
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

    private void checkDefaultUser(Log log, Persistence persistence) {
        try {
            boolean createUser = false;
            Set<String> values = persistence.getValuesKeys(new String[] {});
            if(values.contains("users")) {
                values = persistence.getValuesKeys(new String[] {"users"});
                if(values.size() == 0)
                    createUser = true;
            } else
                createUser = true;
            if(createUser) {
                log.d("Creating default user/password admin/admin");
                persistence.saveTypeInstances(new String[]{"users", "admin", "password"}, new TypeInstances(new TypeInstance("admin")));
            }
        } catch(Throwable t) {
            log.e("Failed to ensure default user exists");
        }
    }
}
