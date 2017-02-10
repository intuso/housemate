package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 09/05/16.
 */
public class ConnectionProvider implements Provider<Connection> {

    public final static String HOST = "server.host";
    public final static String PORT = "server.port";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(ConnectionProvider.HOST, "localhost");
        defaultProperties.set(ConnectionProvider.PORT, "4600");
    }

    private final PropertyRepository properties;

    @Inject
    public ConnectionProvider(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    public Connection get() {
        try {
            Connection connection = new ActiveMQConnectionFactory(
                    "failover:tcp://"
                            + properties.get(HOST)
                            + ":"
                            + properties.get(PORT))
                    .createConnection();
            connection.start();
            return connection;
        } catch (JMSException e) {
            throw new HousemateException("Failed to create connection to broker", e);
        }
    }
}
