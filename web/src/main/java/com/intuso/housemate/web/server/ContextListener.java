package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.comms.serialiser.javabin.ioc.JavabinSerialiserClientModule;
import com.intuso.housemate.comms.transport.socket.client.ioc.SocketClientModule;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.persistence.flatfile.ioc.FlatFilePersistenceModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.platform.pc.ioc.PCClientModule;
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
    private final static String COMPONENT = "UI-Server";

    public static Injector INJECTOR;

    public ContextListener() {
        this(createInjector());
    }

    @Inject
    public ContextListener(Injector injector) {
        INJECTOR = injector;
    }

    private static Injector createInjector() {
        ListenersFactory listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        PropertyRepository properties = Properties.create(listenersFactory, defaultProperties, new String[]{"-log.stdout.level", "DEBUG"});
        return Guice.createInjector(
                new PCClientModule(defaultProperties, properties, "web-server.log"),
                new JavabinSerialiserClientModule(),
                new SocketClientModule(defaultProperties),
                new FlatFilePersistenceModule(defaultProperties));
    }

    @Override
    protected Injector getInjector() {
        return INJECTOR;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

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
                    router.register(APPLICATION_DETAILS, COMPONENT);
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
        if(router.getServerConnectionStatus() != ServerConnectionStatus.ConnectedToServer && router.getServerConnectionStatus() != ServerConnectionStatus.DisconnectedTemporarily)
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
