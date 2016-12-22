package com.intuso.housemate.platform.android.app;

import android.app.Service;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 26/02/14
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateService extends Service {

    private Logger logger;
    private ListenersFactory listenersFactory;
    private PropertyRepository properties;
    private Connection connection;

    @Override
    public void onCreate() {
        super.onCreate();
        logger = LoggerFactory.getLogger(this.getClass());
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        properties = new SharedPreferencesPropertyRepository(listenersFactory, getApplicationContext());
        try {
            // todo ask for whether a local broker is available (run by an hm android service)
            connection = new ActiveMQConnectionFactory("tcp://" + properties.get("server.host") + ":" + properties.get("server.port")).createConnection();
        } catch (JMSException e) {
            logger.error("Failed to create connection to server", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger = null;
        properties = null;
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                logger.error("Failed to close conncetion to server", e);
            }
            connection = null;
        }
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

    public Connection getConnection() {
        return connection;
    }
}
