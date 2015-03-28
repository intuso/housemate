package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.comms.serialiser.javabin.JavabinSerialiser;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.housemate.comms.transport.socket.client.ioc.SocketClientModule;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionService extends Service {

    private final static String LOG_LEVEL = "log.level";
    private final static ApplicationDetails APPLICATION_DETAILS =
            new ApplicationDetails(ConnectionService.class.getPackage().getName(), "Android Service", "Android Service");

    public final static String NETWORK_AVAILABLE_ACTION = "networkAvailable";
    public final static String NETWORK_AVAILABLE = "networkAvailable";

    private final Binder binder = new Binder();

    private final ListenersFactory listenersFactory;
    private final PropertyRepository defaultProperties;

    private PropertyRepository properties;
    private Log log;
    private SocketClient router;
    private ListenerRegistration routerListenerRegistration;

    public ConnectionService() {

        // create a listeners factory
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        // setup the default properties
        defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        defaultProperties.set(LOG_LEVEL, LogLevel.DEBUG.toString());
        // use the normal Guice modules to set the default properties
        new SocketClientModule(defaultProperties);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create all the required objects
        properties = new SharedPreferencesPropertyRepository(listenersFactory, defaultProperties, getApplicationContext());
        log = new Log(new AndroidLogWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), "Housemate Service"));
        router = new SocketClient(log, listenersFactory, properties, new JavabinSerialiser.Factory());

        log.d("Connection Service created");

        // listen on the router root object, then connect the router
        routerListenerRegistration = router.addObjectListener(new RootListener<RouterRoot>() {
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

            }

            @Override
            public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
            }

            @Override
            public void newApplicationInstance(RouterRoot root, String instanceId) {
                // do nothing
            }

            @Override
            public void newServerInstance(RouterRoot root, String serverId) {
                // do nothing
            }
        });
        // connections happen in a different thread so we can call this without blocking
        router.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log.d("Connection Service destroyed");
        routerListenerRegistration.removeListener();
        router.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && NETWORK_AVAILABLE_ACTION.equals(intent.getAction())) {
            if(intent.getExtras().containsKey(NETWORK_AVAILABLE)) {
                log.d("Received network available update: " + intent.getBooleanExtra(NETWORK_AVAILABLE, true));
                router.networkAvailable(intent.getBooleanExtra(NETWORK_AVAILABLE, true));
            }
        }
        return START_STICKY;
    }

    public class Binder extends android.os.Binder {

        public Log getLog() {
            return log;
        }

        public Router getRouter() {
            return router;
        }
    }
}

