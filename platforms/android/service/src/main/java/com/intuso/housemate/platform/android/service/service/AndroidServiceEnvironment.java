package com.intuso.housemate.platform.android.service.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;

import java.util.Map;

/**
 * Platform implementation for PCs
 *
 */
class AndroidServiceEnvironment implements SharedPreferences.OnSharedPreferenceChangeListener, RootListener<RouterRootObject> {

    public final static String LOG_LEVEL = "log.level";

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    private final Log log = new Log("Housemate");
    private SocketClient router;

    /**
     * System Properties
     */
    private final Map<String, String> properties = Maps.newHashMap();

    /**
     * Init the environment instance
     */
    public void init(Context context) {

        // init the properties
        initProps(context);

        log.addWriter(new AndroidLogWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), "Service"));
        router = new SocketClient(new Resources() {
            @Override
            public Log getLog() {
                return log;
            }

            @Override
            public Map<String, String> getProperties() {
                return properties;
            }
        });
        router.addObjectListener(this);
    }

    public Log getLog() {
        return log;
    }

    public Router getRouter() {
        return router;
    }

    public void start() {
        checkProps();
    }

    public void stop() {
        router.disconnect();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        properties.put(s, sharedPreferences.getAll().get(s).toString());
        if(s.equals(SocketClient.SERVER_HOST) || s.equals(SocketClient.SERVER_PORT))
            router.disconnect();
        checkProps();
    }

    private void checkProps() {
        if(properties.get(SocketClient.SERVER_HOST) != null
                && properties.get(SocketClient.SERVER_PORT) != null
                && properties.get(USERNAME) != null
                && properties.get(PASSWORD) != null)
            new Thread() {
                @Override
                public void run() {
                    router.connect();
                }
            }.start();
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
        if(properties.get(SocketClient.SERVER_PORT) == null)
            properties.put(SocketClient.SERVER_PORT, "46874");
        if(properties.get(USERNAME) == null)
            properties.put(USERNAME, "admin");
        if(properties.get(PASSWORD) == null)
            properties.put(PASSWORD, "admin");
    }

    @Override
    public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
        switch (status) {
            case Unauthenticated:
                router.login(getAuthMethod());
        }
    }

    @Override
    public void newServerInstance(RouterRootObject root) {
        // do something!
    }

    private AuthenticationMethod getAuthMethod() {
        return new UsernamePassword(properties.get(USERNAME), properties.get(PASSWORD), false, true);
    }
}
