package com.intuso.housemate.platform.android.app;

import android.app.Activity;
import com.intuso.housemate.client.v1_0.api.type.TypeSerialiser;
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
public abstract class HousemateActivity extends Activity {

    private Logger logger;
    private ListenersFactory listenersFactory;
    private TypeSerialiser.Repository typeSerialiserRepository;
    private PropertyRepository properties;
    private Connection connection;

    @Override
    protected void onStart() {
        super.onStart();
        logger = LoggerFactory.getLogger(this.getClass());
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        typeSerialiserRepository = new TypeSerialiser.Repository() {
            // todo at least register all the "system" types eg String
            @Override
            public <O> TypeSerialiser<O> forClass(Class<?> clazz) {
                return null; // todo
            }

            @Override
            public <O> TypeSerialiser<O> forId(String id) {
                return null; // todo
            }
        };
        properties = new SharedPreferencesPropertyRepository(listenersFactory, getApplicationContext());
        try {
            connection = new ActiveMQConnectionFactory("tcp://" + properties.get("server.host") + ":" + properties.get("server.port")).createConnection();
        } catch (JMSException e) {
            logger.error("Failed to create connection to server", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    public TypeSerialiser.Repository getTypeSerialiserRepository() {
        return typeSerialiserRepository;
    }

    public PropertyRepository getProperties() {
        return properties;
    }

    public Connection getConnection() {
        return connection;
    }
}
