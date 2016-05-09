package com.intuso.housemate.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 05/05/16.
 */
public class ConnectionProvider implements Provider<Connection> {

    private final BrokerService brokerService;

    @Inject
    public ConnectionProvider(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @Override
    public Connection get() {
        try {
            return new ActiveMQConnectionFactory(brokerService.getVmConnectorURI()).createConnection();
        } catch (JMSException e) {
            throw new HousemateException("Failed to create connection to local broker", e);
        }
    }
}
