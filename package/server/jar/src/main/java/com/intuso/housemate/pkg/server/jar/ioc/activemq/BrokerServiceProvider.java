package com.intuso.housemate.pkg.server.jar.ioc.activemq;

import org.apache.activemq.broker.BrokerService;

/**
 * Created by tomc on 26/04/16.
 */
public class BrokerServiceProvider {

    public static BrokerService brokerService = null;

    public BrokerService getBrokerService() {
        return brokerService;
    }
}
