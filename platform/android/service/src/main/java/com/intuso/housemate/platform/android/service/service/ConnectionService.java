package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.intuso.housemate.platform.android.app.SharedPreferencesPropertyRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.PropertyValueChangeListener;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionService extends Service implements PropertyValueChangeListener {

    private final static String HOST = "server.host";
    private final static String PORT = "server.port";

    public final static String NETWORK_AVAILABLE_ACTION = "networkAvailable";
    public final static String NETWORK_AVAILABLE = "networkAvailable";

    private final Binder binder = new Binder();

    private final ManagedCollectionFactory managedCollectionFactory;
    private final ManagedCollection<Listener> listeners;
    private final PropertyRepository defaultProperties;

    private Logger logger;
    private PropertyRepository properties;
    private ManagedCollection.Registration hostRegistration;
    private ManagedCollection.Registration portRegistration;
    private boolean networkAvailable = true;
    private MqttClient client;

    public ConnectionService() {

        managedCollectionFactory = new ManagedCollectionFactory() {
            @Override
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        listeners = managedCollectionFactory.create();

        // setup the default properties
        defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);
        defaultProperties.set(HOST, "domain.com");
        defaultProperties.set(PORT, "1833");
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
        hostRegistration = properties.addListener(HOST, this);
        portRegistration = properties.addListener(PORT, this);
        logger = LoggerFactory.getLogger(ConnectionService.class);
        openClient();
        for(Listener listener : listeners)
            listener.onChange(client != null, networkAvailable, client != null && client.isConnected());
        logger.debug("Connection Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hostRegistration.remove();
        portRegistration.remove();
        closeClient();
        logger.debug("Connection Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && NETWORK_AVAILABLE_ACTION.equals(intent.getAction())) {
            if(intent.getExtras().containsKey(NETWORK_AVAILABLE)) {
                logger.debug("Received network available update: " + intent.getBooleanExtra(NETWORK_AVAILABLE, true));
                networkAvailable = intent.getBooleanExtra(NETWORK_AVAILABLE, true);
                for(Listener listener : listeners)
                    listener.onChange(client != null, networkAvailable, client != null && client.isConnected());
            }
        }
        return START_STICKY;
    }

    @Override
    public void propertyValueChanged(String key, String oldValue, String newValue) {
        logger.debug("{} property changed, recreating client", key);
        closeClient();
        openClient();
    }

    private void openClient() {
        try {
            client = new MqttClient("tcp://" + properties.get(HOST) + ":" + properties.get(PORT), "aphone");
            try {
                client.connect();
                for(Listener listener : listeners)
                    listener.clientOpened(client);
            } catch (MqttException e) {
                logger.error("Failed to connect mqtt connection", e);
                client = null;
            }
        } catch (MqttException e) {
            logger.error("Failed to create mqtt connection", e);
        }
        for(Listener listener : listeners)
            listener.onChange(client != null, networkAvailable, client != null && client.isConnected());
    }

    private void closeClient() {
        for(Listener listener : listeners)
            listener.clientClosing(client);
        try {
            client.disconnect();
            client = null;
        } catch (MqttException e) {
            logger.error("Problem closing connection to housemate server", e);
        }
        for(Listener listener : listeners)
            listener.onChange(client != null, networkAvailable, client != null && client.isConnected());
    }

    public class Binder extends android.os.Binder {

        public MqttClient getClient() {
            return client;
        }

        public ManagedCollection.Registration addListener(Listener listener, boolean callForExisting) {
            ManagedCollection.Registration result = listeners.add(listener);
            if(callForExisting) {
                listener.onChange(client != null, networkAvailable, client != null && client.isConnected());
                if (client != null)
                    listener.clientOpened(client);
                else
                    listener.clientClosing(client);
            }
            return result;
        }
    }

    public interface Listener {
        void onChange(boolean clientAvailable, boolean networkAvailable, boolean clientConnected);
        void clientOpened(MqttClient client);
        void clientClosing(MqttClient client);
    }
}

