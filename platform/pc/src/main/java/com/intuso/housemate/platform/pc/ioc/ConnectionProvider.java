package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 09/05/16.
 */
public class ConnectionProvider implements Provider<Connection> {
    @Override
    public Connection get() {
        try {
            Connection connection = new ActiveMQConnectionFactory("tcp://localhost:46873").createConnection();
            connection.start();
            return connection;
        } catch (JMSException e) {
            throw new HousemateException("Failed to create connection to broker");
        }
    }
}
