package com.intuso.housemate.server.ioc;

import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.server.activemq.StoredMessageSubscriptionRecoveryPolicy;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.util.DestinationPathSeparatorBroker;
import org.apache.activemq.store.kahadb.KahaDBStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by tomc on 05/05/16.
 */
public class BrokerServiceProvider implements Provider<BrokerService> {

    Logger logger = LoggerFactory.getLogger(BrokerServiceProvider.class);

    @Override
    public BrokerService get() {

        BrokerService brokerService = new BrokerService();
        brokerService.setPlugins(new BrokerPlugin[] {
                new DestinationPathSeparatorBroker() // use / as a separator rather than .
        });
        brokerService.setBrokerName("housemate");

        // enable management context for ui
        brokerService.setUseJmx(true);
        ManagementContext managementContext = new ManagementContext();
        managementContext.setCreateConnector(false);
        brokerService.setManagementContext(managementContext);

        try {
            brokerService.setPersistenceAdapter(new KahaDBStore());
        } catch (IOException e) {
            logger.error("Failed to setup broker message persistence", e);
        }

        // setup the destination policies
        PolicyMap policyMap = new PolicyMap();
        PolicyEntry defaultEntry = new PolicyEntry();
        File messageRootDir = new File("messages");
        messageRootDir.mkdirs();
        defaultEntry.setSubscriptionRecoveryPolicy(new StoredMessageSubscriptionRecoveryPolicy(ChildUtil.logger(logger, "message-persistence"), brokerService, messageRootDir));
        policyMap.setDefaultEntry(defaultEntry);
        brokerService.setDestinationPolicy(policyMap);

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
