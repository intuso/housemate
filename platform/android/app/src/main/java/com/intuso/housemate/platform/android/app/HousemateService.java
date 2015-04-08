package com.intuso.housemate.platform.android.app;

import android.app.Service;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.platform.android.app.comms.AndroidAppRouter;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 26/02/14
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateService extends Service {

    private Log log;
    private ListenersFactory listenersFactory;
    private PropertyRepository properties;
    private Router router;

    @Override
    public void onCreate() {
        super.onCreate();
        log = new Log(new AndroidLogWriter(LogLevel.DEBUG, getApplicationContext().getApplicationInfo().name));
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        properties = new SharedPreferencesPropertyRepository(listenersFactory, getApplicationContext());
        router = new AndroidAppRouter(log, listenersFactory, properties, getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log = null;
        properties = null;
        router.disconnect();
        router = null;
    }

    public Log getLog() {
        return log;
    }

    public ListenersFactory getListenersFactory() {
        return listenersFactory;
    }

    public PropertyRepository getProperties() {
        return properties;
    }

    public Router getRouter() {
        return router;
    }
}
