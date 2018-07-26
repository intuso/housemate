package com.intuso.housemate.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.server.activemq.StoredMessageSubscriptionRecoveryPolicy;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
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

    public final static String BROKER_TCP_HOST = "broker.tcp.host";
    public final static String BROKER_TCP_PORT = "broker.tcp.port";
    public final static String BROKER_MQTT_HOST = "broker.mqtt.host";
    public final static String BROKER_MQTT_PORT = "broker.mqtt.port";
    public final static String BROKER_STOMP_HOST = "broker.stomp.host";
    public final static String BROKER_STOMP_PORT = "broker.stomp.port";
    public final static String BROKER_STORAGE_DIR = "broker.storage.dir";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(BROKER_TCP_HOST, "0.0.0.0");
        defaultProperties.set(BROKER_TCP_PORT, "4600");
        defaultProperties.set(BROKER_MQTT_HOST, "0.0.0.0");
        defaultProperties.set(BROKER_MQTT_PORT, "1833");
        defaultProperties.set(BROKER_STOMP_HOST, "0.0.0.0");
        defaultProperties.set(BROKER_STOMP_PORT, "61613");
        defaultProperties.set(BROKER_STORAGE_DIR, "./broker/storage");
    }

    private final static Logger logger = LoggerFactory.getLogger(BrokerServiceProvider.class);

    private final PropertyRepository properties;

    @Inject
    public BrokerServiceProvider(PropertyRepository properties) {
        this.properties = properties;
    }

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

        File storageDir = new File(properties.get(BROKER_STORAGE_DIR));
        if(!storageDir.exists())
            storageDir.mkdirs();
        else if(!storageDir.isDirectory())
            throw new HousemateException("Broker storage directory is not a directory: " + storageDir.getAbsolutePath());

        try {
            KahaDBStore kahaDbStore = new KahaDBStore();
            kahaDbStore.setDirectory(new File(storageDir, "KahaDB"));
            brokerService.setPersistenceAdapter(kahaDbStore);
        } catch (IOException e) {
            logger.error("Failed to setup broker message persistence", e);
        }

        // setup the destination policies
        PolicyMap policyMap = new PolicyMap();
        PolicyEntry defaultEntry = new PolicyEntry();
        File messageRootDir = new File(storageDir, "messages");
        messageRootDir.mkdirs();
        defaultEntry.setSubscriptionRecoveryPolicy(new StoredMessageSubscriptionRecoveryPolicy(ChildUtil.logger(logger, "message-persistence"), brokerService, messageRootDir));
        policyMap.setDefaultEntry(defaultEntry);
        brokerService.setDestinationPolicy(policyMap);

        // setup the connectors this broker will provide
        try {
            brokerService.addConnector("tcp://" + properties.get(BROKER_TCP_HOST) + ":" + properties.get(BROKER_TCP_PORT));
        } catch(Exception e) {
            logger.error("Failed to add the tcp connector to the broker. Some remote clients might be unable to connect", e);
        }
        try {
            brokerService.addConnector("mqtt://" + properties.get(BROKER_MQTT_HOST) + ":" + properties.get(BROKER_MQTT_PORT));
        } catch(Exception e) {
            logger.error("Failed to add the mqtt connector to the broker. Some remote clients might be unable to connect", e);
        }
        // this seems to be included by default
        try {
            brokerService.addConnector("stomp://" + properties.get(BROKER_STOMP_HOST) + ":" + properties.get(BROKER_STOMP_PORT));
        } catch(Exception e) {
            logger.error("Failed to add the stomp connector to the broker. Some remote clients might be unable to connect", e);
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
