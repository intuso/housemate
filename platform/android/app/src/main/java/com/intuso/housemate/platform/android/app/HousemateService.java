package com.intuso.housemate.platform.android.app;

import android.app.Service;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
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
public abstract class HousemateService extends Service {

    private Logger logger;
    private ManagedCollectionFactory managedCollectionFactory;
    private TypeSerialiser.Repository typeSerialiserRepository;
    private PropertyRepository properties;
//    private Connection connection;

    @Override
    public void onCreate() {
        super.onCreate();
        logger = LoggerFactory.getLogger(this.getClass());
        managedCollectionFactory = new ManagedCollectionFactory() {
            @Override
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        typeSerialiserRepository = new TypeSerialiser.Repository() {
            // todo at least register all the "system" types eg String
            @Override
            public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
                throw new HousemateException("Unknown type requested: " + typeSpec.toString());
            }
        };
        properties = new SharedPreferencesPropertyRepository(managedCollectionFactory, getApplicationContext());
        /*try {
            // todo ask for whether a local broker is available (run by an hm android service)
            connection = new ActiveMQConnectionFactory("tcp://" + properties.get("server.host") + ":" + properties.get("server.port")).createConnection();
        } catch (JMSException e) {
            logger.error("Failed to create connection to server", e);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger = null;
        properties = null;
        /*if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                logger.error("Failed to close conncetion to server", e);
            }
            connection = null;
        }*/
    }

    public Logger getLogger() {
        return logger;
    }

    public ManagedCollectionFactory getManagedCollectionFactory() {
        return managedCollectionFactory;
    }

    public TypeSerialiser.Repository getTypeSerialiserRepository() {
        return typeSerialiserRepository;
    }

    public PropertyRepository getProperties() {
        return properties;
    }

    /*public Connection getConnection() {
        return connection;
    }*/
}
