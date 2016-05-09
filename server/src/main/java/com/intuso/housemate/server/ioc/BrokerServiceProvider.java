package com.intuso.housemate.server.ioc;

import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import org.apache.activemq.broker.BrokerService;

/**
 * Created by tomc on 05/05/16.
 */
public class BrokerServiceProvider implements Provider<BrokerService> {
    @Override
    public BrokerService get() {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("housemate");

        try {
            brokerService.start();
        } catch (Exception e) {
            throw new HousemateException("Failed to start broker", e);
        }

        return brokerService;
    }
}
