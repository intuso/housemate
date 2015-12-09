package com.intuso.housemate.platform.android.app;

import android.app.Activity;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.platform.android.app.comms.AndroidAppRouter;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 26/02/14
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateActivity extends Activity {

    private Logger logger;
    private ListenersFactory listenersFactory;
    private PropertyRepository properties;
    private Router<?> router;

    @Override
    protected void onStart() {
        super.onStart();
        logger = LoggerFactory.getLogger(this.getClass());
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        properties = new SharedPreferencesPropertyRepository(listenersFactory, getApplicationContext());
        router = new AndroidAppRouter(logger, listenersFactory, getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger = null;
        properties = null;
        router.disconnect();
        router = null;
    }

    public Logger getLogger() {
        return logger;
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
