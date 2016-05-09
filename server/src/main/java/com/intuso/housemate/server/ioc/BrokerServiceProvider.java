package com.intuso.housemate.server.ioc;

import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 05/05/16.
 */
public class BrokerServiceProvider implements Provider<BrokerService> {

    Logger logger = LoggerFactory.getLogger(BrokerServiceProvider.class);

    @Override
    public BrokerService get() {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("housemate");

        // setup the connectors this broker will provide
        try {
            brokerService.addConnector("tcp://0.0.0.0:46873");
        } catch(Exception e) {
            logger.error("Failed to add a connector to the broker. No remote clients will be able to connect", e);
        }

        // start the broker
        try {
            brokerService.start();
        } catch (Exception e) {
            throw new HousemateException("Failed to start broker", e);
        }

        return brokerService;
    }
}
