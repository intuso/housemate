package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionService extends Service {

    private final static String LOG_LEVEL = "log.level";

    public final static String NETWORK_AVAILABLE_ACTION = "networkAvailable";
    public final static String NETWORK_AVAILABLE = "networkAvailable";

    private final Binder binder = new Binder();

    private final ManagedCollectionFactory managedCollectionFactory;
    private final PropertyRepository defaultProperties;

    private PropertyRepository properties;
    private Logger logger;
    private MemberRegistration routerListenerRegistration;

    public ConnectionService() {

        // create a listeners factory
        managedCollectionFactory = new ManagedCollectionFactory() {
            @Override
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        // setup the default properties
        defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create all the required objects
        properties = new SharedPreferencesPropertyRepository(managedCollectionFactory, defaultProperties, getApplicationContext());
        logger = LoggerFactory.getLogger(ConnectionService.class);

        logger.debug("Connection Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("Connection Service destroyed");
        routerListenerRegistration.removeListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && NETWORK_AVAILABLE_ACTION.equals(intent.getAction())) {
            if(intent.getExtras().containsKey(NETWORK_AVAILABLE)) {
                logger.debug("Received network available update: " + intent.getBooleanExtra(NETWORK_AVAILABLE, true));
//                router.networkAvailable(intent.getBooleanExtra(NETWORK_AVAILABLE, true));
            }
        }
        return START_STICKY;
    }

    public class Binder extends android.os.Binder {
//        public Router<?> getRouter() {
//            return router;
//        }
    }
}

