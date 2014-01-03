package com.intuso.housemate.platform.android.service.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.platform.android.common.SharedPreferencesReader;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;
import com.intuso.utilities.properties.api.Reader;

import java.util.Iterator;

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
    private final PropertyContainer properties = new PropertyContainer();

    /**
     * Init the environment instance
     */
    public void init(Context context) {

        // init the properties
        properties.read(new SharedPreferencesReader("androidProperties", 1, context));
        properties.read(new Reader("defaults", 0) {
            @Override
            public Iterator<Entry> iterator() {
                return Lists.newArrayList(
                        new Entry(LOG_LEVEL, LogLevel.DEBUG.name()),
                        new Entry(SocketClient.SERVER_PORT, "46874"),
                        new Entry(USERNAME, "admin"),
                        new Entry(PASSWORD, "admin")
                ).iterator();
            }
        });
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);

        log.addWriter(new AndroidLogWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), "Service"));
        router = new SocketClient(new Resources() {
            @Override
            public Log getLog() {
                return log;
            }

            @Override
            public PropertyContainer getProperties() {
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
        properties.set(s, new PropertyValue("androidProperties", 1, sharedPreferences.getAll().get(s).toString()));
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
