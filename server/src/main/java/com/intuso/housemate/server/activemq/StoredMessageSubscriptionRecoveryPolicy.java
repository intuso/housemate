package com.intuso.housemate.server.activemq;

import com.intuso.housemate.client.api.internal.MessageConstants;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.broker.region.SubscriptionRecovery;
import org.apache.activemq.broker.region.Topic;
import org.apache.activemq.broker.region.policy.SubscriptionRecoveryPolicy;
import org.apache.activemq.command.*;
import org.apache.activemq.filter.DestinationFilter;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomc on 27/06/16.
 */
public class StoredMessageSubscriptionRecoveryPolicy implements SubscriptionRecoveryPolicy {

    private final Logger logger;
    private final BrokerService brokerService;
    private final File rootDir;
    private volatile String topicName;
    private volatile File messageFile;
    private volatile boolean createdMessageStore;
    private volatile Message storedMessage;

    public StoredMessageSubscriptionRecoveryPolicy(Logger logger, BrokerService brokerService, File rootDir) {
        this.logger = logger;
        this.brokerService = brokerService;
        this.rootDir = rootDir;
    }

    public boolean add(ConnectionContext context, MessageReference node) throws Exception {
        final Message message = node.getMessage();
        final Object storeValue = message.getProperty(MessageConstants.STORE);
        // store property set to true
        final boolean store = storeValue != null && Boolean.parseBoolean(storeValue.toString());
        if (store) {
            initFile(message.getDestination());
            message.removeProperty(MessageConstants.STORE);
            if (message.getContent().getLength() > 0) {
                // non zero length message content
                storedMessage = message.copy();
                store();
            } else {
                // clear stored message
                storedMessage = null;
            }
        }
        return true;
    }

    public void recover(ConnectionContext context, Topic topic, SubscriptionRecovery sub) throws Exception {
        // Re-dispatch the last stored message seen.
        initFile(topic.getActiveMQDestination());
        if (storedMessage != null)
            sub.addRecoveredMessage(context, storedMessage);
    }

    public void start() throws Exception {}

    public void stop() throws Exception {}

    public Message[] browse(ActiveMQDestination destination) throws Exception {
        final List<Message> result = new ArrayList<>();
        if (storedMessage != null) {
            DestinationFilter filter = DestinationFilter.parseFilter(destination);
            if (filter.matches(storedMessage.getMessage().getDestination())) {
                result.add(storedMessage.getMessage());
            }
        }
        return result.toArray(new Message[result.size()]);
    }

    public SubscriptionRecoveryPolicy copy() {
        return new StoredMessageSubscriptionRecoveryPolicy(logger, brokerService, rootDir);
    }

    public void setBroker(Broker broker) {

    }

    private void store() throws Exception {
        if(storedMessage instanceof ActiveMQStreamMessage) {
            initFile(storedMessage.getDestination());
            if (messageFile != null) {
                try {
                    boolean wasReadOnly = storedMessage.isReadOnlyBody();
                    storedMessage.setReadOnlyBody(true);
                    java.lang.Object messageObject = ((ActiveMQStreamMessage) storedMessage).readObject();
                    storedMessage.setReadOnlyBody(wasReadOnly);
                    if(messageObject instanceof byte[]) {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageFile));
                        oos.writeObject(new PersistenceObject(topicName, storedMessage.getMessageId().toProducerKey(), (byte[]) messageObject));
                        oos.close();
                    }
                } catch (IOException e) {
                    logger.error("Failed to persist message to {}", messageFile.getAbsolutePath(), e);
                }
            } else
                logger.warn("Can't persist message, file not configured");
        } else
            logger.warn("Not persisting non-stream message type: " + storedMessage.getClass().getName());
    }

    private void initFile(ActiveMQDestination destination) throws Exception {
        if(!createdMessageStore) {
            synchronized (this) {
                if(!createdMessageStore) {
                    if(destination instanceof ActiveMQTopic) {
                        topicName = ((ActiveMQTopic) destination).getTopicName();
                        messageFile = new File(rootDir, topicName + ".bin");
                        try {
                            if (messageFile.exists()) {
                                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFile));
                                Object object = ois.readObject();
                                if(object instanceof PersistenceObject) {
                                    PersistenceObject persistenceObject = (PersistenceObject) object;
                                    ActiveMQStreamMessage message = new ActiveMQStreamMessage();
                                    message.setRegionDestination(brokerService.getDestination(new ActiveMQTopic(persistenceObject.topicName)));
                                    message.setMessageId(new MessageId(persistenceObject.messageKey));
                                    message.writeBytes(persistenceObject.data);
                                    message.setReadOnlyBody(true);
                                    message.storeContent();
                                    storedMessage = message;
                                }
                            }
                        } catch(IOException e) {
                            logger.error("Failed to load persisted message from {}", messageFile.getAbsolutePath(), e);
                        }
                    }
                    createdMessageStore = true;
                }
            }
        }
    }

    private static class PersistenceObject implements Serializable {

        private String topicName;
        private String messageKey;
        private byte[] data;

        public PersistenceObject() {}

        public PersistenceObject(String topicName, String messageKey, byte[] data) {
            this.topicName = topicName;
            this.messageKey = messageKey;
            this.data = data;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public String getMessageKey() {
            return messageKey;
        }

        public void setMessageKey(String messageKey) {
            this.messageKey = messageKey;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}
