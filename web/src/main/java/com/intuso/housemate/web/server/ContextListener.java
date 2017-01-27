package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.servlet.GuiceServletContextListener;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.Router;
import com.intuso.housemate.client.v1_0.data.api.access.ConnectionStatus;
import com.intuso.housemate.client.v1_0.data.serialiser.javabin.ioc.JavabinSerialiserClientModule;
import com.intuso.housemate.client.v1_0.transport.socket.client.ioc.SocketClientModule;
import com.intuso.housemate.persistence.v1_0.api.Persistence;
import com.intuso.housemate.persistence.v1_0.filesystem.ioc.FileSystemPersistenceModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.platform.pc.ioc.PCClientModule;
import com.intuso.utilities.collection.Listener;
import com.intuso.utilities.collection.Listeners;
import com.intuso.utilities.collection.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
public class ContextListener extends GuiceServletContextListener {

    public static Injector INJECTOR;
    private boolean connectedRouter;

    public ContextListener() {
        this(createInjector());
    }

    @Inject
    public ContextListener(Injector injector) {
        INJECTOR = injector;
    }

    private static Injector createInjector() {
        ListenersFactory managedCollectionFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);
        PropertyRepository properties = Properties.create(managedCollectionFactory, defaultProperties, new String[] {});
        return Guice.createInjector(
                new PCClientModule(properties),
                new JavabinSerialiserClientModule(),
                new SocketClientModule(defaultProperties),
                new FileSystemPersistenceModule(defaultProperties));
    }

    @Override
    protected Injector getInjector() {
        return INJECTOR;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        super.contextInitialized(servletContextEvent);

        final Logger logger = LoggerFactory.getLogger(ContextListener.class);
        final Router<?> router = INJECTOR.getInstance(new Key<Router<?>>() {});
        Persistence persistence = INJECTOR.getInstance(Persistence.class);

        checkDefaultUser(logger, persistence);

        // will be null if started in dev mode, in which case the server is run separately
        router.addListener(new Router.Listener<Router>() {

            @Override
            public void serverConnectionStatusChanged(Router router, ConnectionStatus connectionStatus) {
                logger.debug("Server connection status: " + connectionStatus);
                if (connectionStatus == ConnectionStatus.DisconnectedPermanently) {
                    connectedRouter = true;
                    router.connect();
                }
            }

            @Override
            public void newServerInstance(Router root, String serverId) {
                logger.debug("Server instance changed");
            }
        });
        if(router.getConnectionStatus() == ConnectionStatus.DisconnectedPermanently) {
            connectedRouter = true;
            router.connect();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if(connectedRouter)
            INJECTOR.getInstance(new Key<Router<?>>() {}).disconnect();
        super.contextDestroyed(servletContextEvent);
    }

    private void checkDefaultUser(Logger logger, Persistence persistence) {
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
                logger.debug("Creating default user/password admin/admin");
                persistence.saveTypeInstances(new String[]{"users", "admin", "password"}, new Type.Instances(new Type.Instance("admin")));
            }
        } catch(Throwable t) {
            logger.error("Failed to ensure default user exists");
        }
    }
}
