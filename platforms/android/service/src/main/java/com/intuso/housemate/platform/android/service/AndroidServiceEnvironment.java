package com.intuso.housemate.platform.android.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Maps;
import com.intuso.home.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;

import java.util.Map;

/**
 * Platform implementation for PCs
 *
 */
public class AndroidServiceEnvironment implements SharedPreferences.OnSharedPreferenceChangeListener, RootListener<RouterRootObject> {

    public final static String LOG_LEVEL = "log.level";
    public final static String BROKER_PORT = "broker_port";
    public final static String BROKER_HOST = "broker_host";

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    private final Log appLog = new Log("Housemate");
    private SocketClient comms;

    /**
     * System Properties
     */
    private final Map<String, String> properties = Maps.newHashMap();
    private final ClientResources resources = new ClientResources() {
        @Override
        public Router getRouter() {
            return comms;
        }

        @Override
        public Log getLog() {
            return appLog;
        }

        @Override
        public Map<String, String> getProperties() {
            return properties;
        }
    };;

    /**
     * Init the environment instance
     */
    public void init(Context context) {

        // init the properties
        initProps(context);

        appLog.addWriter(new AndroidLogWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), "Service"));
        comms = new SocketClient(resources, properties.get(BROKER_HOST), Integer.parseInt(properties.get(BROKER_PORT)));
        comms.addObjectListener(this);
    }

    public void start() {
        checkProps();
    }

    public void stop() {
        comms.disconnect();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        properties.put(s, sharedPreferences.getAll().get(s).toString());
        if(s.equals(BROKER_HOST) || s.equals(BROKER_PORT))
            comms.disconnect();
        checkProps();
    }

    private void checkProps() {
        if(properties.get(BROKER_HOST) != null
                && properties.get(BROKER_PORT) != null
                && properties.get(USERNAME) != null
                && properties.get(PASSWORD) != null)
            new Thread() {
                @Override
                public void run() {
                    comms.connect();
                }
            }.start();
    }

    public ClientResources getResources() {
        return resources;
    }

    private void initProps(Context context) {
        properties.clear();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(this);
        for(Map.Entry<String, ?> entry : preferences.getAll().entrySet())
            properties.put(entry.getKey(), entry.getValue().toString());

        // set any defaults
        if(properties.get(LOG_LEVEL) == null)
            properties.put(LOG_LEVEL, LogLevel.DEBUG.name());
        if(properties.get(BROKER_PORT) == null)
            properties.put(BROKER_PORT, "46874");
        if(properties.get(USERNAME) == null)
            properties.put(USERNAME, "admin");
        if(properties.get(PASSWORD) == null)
            properties.put(PASSWORD, "admin");
    }

    @Override
    public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
        switch (status) {
            case Unauthenticated:
                comms.login(new UsernamePassword(properties.get(USERNAME), properties.get(PASSWORD), true));
        }
    }

    @Override
    public void brokerInstanceChanged(RouterRootObject root) {
        // do something!
    }
}
