package com.intuso.housemate.web.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.servlet.GuiceServletContextListener;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.comms.v1_0.serialiser.javabin.ioc.JavabinSerialiserClientModule;
import com.intuso.housemate.comms.v1_0.transport.socket.client.ioc.SocketClientModule;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.persistence.v1_0.api.Persistence;
import com.intuso.housemate.persistence.v1_0.filesystem.ioc.FileSystemPersistenceModule;
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
                new FileSystemPersistenceModule(defaultProperties));
    }

    @Override
    protected Injector getInjector() {
        return INJECTOR;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        super.contextInitialized(servletContextEvent);

        final Log log = INJECTOR.getInstance(Log.class);
        final Router<?> router = INJECTOR.getInstance(new Key<Router<?>>() {});
        Persistence persistence = INJECTOR.getInstance(Persistence.class);

        checkDefaultUser(log, persistence);

        // will be null if started in dev mode, in which case the server is run separately
        router.addListener(new Router.Listener<Router>() {

            @Override
            public void serverConnectionStatusChanged(Router router, ServerConnectionStatus serverConnectionStatus) {
                log.d("Server connection status: " + serverConnectionStatus);
                if (serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently)
                    router.connect();
            }

            @Override
            public void newServerInstance(Router root, String serverId) {
                log.d("Server instance changed");
            }
        });
        if(router.getServerConnectionStatus() == ServerConnectionStatus.DisconnectedPermanently)
            router.connect();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        INJECTOR.getInstance(new Key<Router<?>>() {}).disconnect();
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
