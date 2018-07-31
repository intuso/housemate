package com.intuso.housemate.server.activemq;

import com.intuso.housemate.client.messaging.api.internal.MessageConstants;
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

import javax.jms.BytesMessage;
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
        if(message instanceof ActiveMQMessage) {
            ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;
            if (activeMQMessage.getBooleanProperty(MessageConstants.STORE)) {
                initFile(message.getDestination());
                if (message.getContent() != null && message.getContent().getLength() > 0) {
                    // non zero length message content
                    storedMessage = message.copy();
                    store();
                } else {
                    // clear stored message
                    storedMessage = null;
                }
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
                    if (messageObject instanceof byte[]) {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageFile));
                        oos.writeObject(new PersistenceObject(topicName, storedMessage.getMessageId().toProducerKey(), MessageType.Stream, (byte[]) messageObject));
                        oos.close();
                    }
                } catch (IOException e) {
                    logger.error("Failed to persist message to {}", messageFile.getAbsolutePath(), e);
                }
            } else
                logger.warn("Can't persist message, file not configured");
        } else if(storedMessage instanceof ActiveMQBytesMessage) {
            initFile(storedMessage.getDestination());
            if (messageFile != null) {
                try {
                    boolean wasReadOnly = storedMessage.isReadOnlyBody();
                    storedMessage.setReadOnlyBody(true);
                    BytesMessage bytesMessage = (ActiveMQBytesMessage) storedMessage;
                    byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(bytes);
                    storedMessage.setReadOnlyBody(wasReadOnly);
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageFile));
                    oos.writeObject(new PersistenceObject(topicName, storedMessage.getMessageId().toProducerKey(), MessageType.Bytes, bytes));
                    oos.close();
                } catch (IOException e) {
                    logger.error("Failed to persist message to {}", messageFile.getAbsolutePath(), e);
                }
            } else
                logger.warn("Can't persist message, file not configured");
        } else
            logger.warn("Don't know how to persist message type: " + storedMessage.getClass().getName());
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
                                    if(persistenceObject.messageType == null)
                                        persistenceObject.messageType = MessageType.Stream;
                                    switch (persistenceObject.messageType) {
                                        case Stream:
                                            ActiveMQStreamMessage streamMessage = new ActiveMQStreamMessage();
                                            streamMessage.setRegionDestination(brokerService.getDestination(new ActiveMQTopic(persistenceObject.topicName)));
                                            streamMessage.setMessageId(new MessageId(persistenceObject.messageKey));
                                            streamMessage.writeBytes(persistenceObject.data);
                                            streamMessage.setReadOnlyBody(true);
                                            streamMessage.storeContent();
                                            storedMessage = streamMessage;
                                            break;
                                        case Bytes:
                                            ActiveMQBytesMessage bytesMessage = new ActiveMQBytesMessage();
                                            bytesMessage.setRegionDestination(brokerService.getDestination(new ActiveMQTopic(persistenceObject.topicName)));
                                            bytesMessage.setMessageId(new MessageId(persistenceObject.messageKey));
                                            bytesMessage.writeBytes(persistenceObject.data);
                                            bytesMessage.setReadOnlyBody(true);
                                            bytesMessage.storeContent();
                                            break;
                                    }
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

    private enum MessageType {
        Stream,
        Bytes
    }

    private static class PersistenceObject implements Serializable {

        private static final long serialVersionUID = -1L;

        private String topicName;
        private String messageKey;
        private MessageType messageType;
        private byte[] data;

        public PersistenceObject() {}

        public PersistenceObject(String topicName, String messageKey, MessageType messageType, byte[] data) {
            this.topicName = topicName;
            this.messageKey = messageKey;
            this.messageType = messageType;
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

        public MessageType getMessageType() {
            return messageType;
        }

        public void setMessageType(MessageType messageType) {
            this.messageType = messageType;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}
