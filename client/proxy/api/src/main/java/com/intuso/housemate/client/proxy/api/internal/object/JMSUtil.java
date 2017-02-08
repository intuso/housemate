package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.MessageConstants;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import org.slf4j.Logger;

import javax.jms.*;
import java.io.Closeable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tomc on 01/12/16.
 */
public class JMSUtil {

    public static <T> T getFirstPersisted(Logger logger, Connection connection, Type type, String name, Class<T> objectClass) throws JMSException {
        ConsumerIterator<T> iterator = getPersisted(logger, connection, type, name, objectClass);
        T result = iterator.hasNext() ? iterator.next() : null;
        iterator.close();
        return result;
    }

    public static <T> ConsumerIterator<T> getPersisted(Logger logger, Connection connection, Type type, String name, Class<T> objectClass) throws JMSException {
        Session session = null;
        MessageConsumer consumer = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            switch (type) {
                case Queue:
                    consumer = session.createConsumer(session.createQueue(name));
                    break;
                case Topic:
                    consumer = session.createConsumer(session.createTopic(name + "?consumer.retroactive=true"));
                    break;
                default:
                    throw new JMSException("Unknown type " + type);
            }
            return new ConsumerIterator<>(logger, session, consumer, objectClass);
        } catch(JMSException e) {
            if(session != null)
                session.close();
            if (consumer != null)
                consumer.close();
            throw e;
        }
    }

    private static <OBJECT> OBJECT deserialise(Logger logger, Message message, Class<OBJECT> objectClass) {
        if (message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                java.lang.Object messageObject = streamMessage.readObject();
                if (messageObject instanceof byte[]) {
                    java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                    if (objectClass.isAssignableFrom(object.getClass()))
                        return (OBJECT) object;
                    else
                        logger.warn("Deserialised message object that wasn't a {} but a {}", objectClass.getName(), object.getClass().getName());
                } else
                    logger.warn("Message data was not a {}", byte[].class.getName());
            } catch (JMSException e) {
                logger.error("Could not read object from received message", e);
            }
        } else
            logger.error("Received message that wasn't a {} but a {}", StreamMessage.class.getName(), message.getClass().getName());
        return null;
    }

    public static class Sender {

        private final Logger logger;
        private final Session session;
        private final MessageProducer producer;

        public Sender(Logger logger, Connection connection, Type type, String name) throws JMSException {
            this.logger = logger;
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            switch (type) {
                case Queue:
                    this.producer = session.createProducer(session.createQueue(name));
                    break;
                case Topic:
                    this.producer = session.createProducer(session.createTopic(name));
                    break;
                default:
                    throw new JMSException("Unknown type " + type);
            }
        }

        public void close() {
            if(producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    logger.error("Failed to close producer");
                }
            }
            if(session != null) {
                try {
                    session.close();
                } catch(JMSException e) {
                    logger.error("Failed to close session");
                }
            }
        }

        public void send(Serializable object, boolean persistent) throws JMSException {
            StreamMessage streamMessage = session.createStreamMessage();
            if(persistent)
                streamMessage.setBooleanProperty(MessageConstants.STORE, true);
            streamMessage.writeBytes(Serialiser.serialise(object));
            producer.send(streamMessage);
            logger.trace("Sent {} on {}", object, producer.getDestination());
        }
    }

    public static class Receiver<OBJECT extends Serializable> {

        private final Logger logger;
        private final Destination destination;
        private final Session session;
        private final MessageConsumer consumer;
        private final Class<OBJECT> objectClass;
        private final Listener<OBJECT> listener;

        public Receiver(final Logger logger, Connection connection, Type type, String name, Class<OBJECT> objectClass, Listener<OBJECT> listener) throws JMSException {
            this.logger = logger;
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            switch (type) {
                case Queue:
                    this.destination = session.createQueue(name);
                    break;
                case Topic:
                    this.destination = session.createTopic(name + "?consumer.retroactive=true");
                    break;
                default:
                    throw new JMSException("Unknown type " + type);
            }
            this.consumer = session.createConsumer(destination);
            this.objectClass = objectClass;
            this.listener = listener;
            try {
                MessageListener messageListener = new MessageListenerImpl();
                Message message;
                while ((message = consumer.receiveNoWait()) != null)
                    messageListener.onMessage(message);
                consumer.setMessageListener(messageListener);
            } catch(JMSException e) {
                logger.error("Failed to subscribe to queue/topic");
            }
        }

        public void close() {
            if(consumer != null) {
                try {
                    consumer.close();
                } catch (JMSException e) {
                    logger.error("Failed to close consumer");
                }
            }
            if(session != null) {
                try {
                    session.close();
                } catch(JMSException e) {
                    logger.error("Failed to close session");
                }
            }
        }

        private class MessageListenerImpl implements MessageListener {

            @Override
            public void onMessage(Message message) {
                logger.trace("Received message on {}", destination);
                OBJECT object = deserialise(logger, message, objectClass);
                if(object != null) {
                    try {
                        listener.onMessage(object, message.getBooleanProperty(MessageConstants.STORE));
                    } catch (JMSException e) {
                        logger.error("Could not check if message was persisted, assuming not", e);
                        listener.onMessage(object, false);
                    }
                } else
                    logger.warn("Received a message but deserialised it to null");
            }
        }

        public interface Listener<OBJECT extends Serializable> {
            void onMessage(OBJECT object, boolean wasPersisted);
        }
    }

    public enum Type {
        Queue,
        Topic
    }

    private static class ConsumerIterator<T> implements Closeable, Iterator<T> {

        private final Logger logger;
        private final Session session;
        private final MessageConsumer messageConsumer;
        private final Class<T> objectClass;

        private T next;

        private ConsumerIterator(Logger logger, Session session, MessageConsumer messageConsumer, Class<T> objectClass) throws JMSException {
            this.logger = logger;
            this.session = session;
            this.messageConsumer = messageConsumer;
            this.objectClass = objectClass;
            getNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if(next == null)
                throw new NoSuchElementException();
            T result = next;
            try {
                getNext();
            } catch (JMSException e) {
                throw new HousemateException("Failed to get next persisted message", e);
            }
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void getNext() throws JMSException {
            Message persisted = messageConsumer.receiveNoWait();
            next = persisted == null ? null : deserialise(logger, persisted, objectClass);
            if(next == null)
                close();
        }

        @Override
        public void close() {
            try {
                messageConsumer.close();
            } catch (JMSException e) {
                logger.error("Could not close ActiveMQ message consumer", e);
            }
            try {
                session.close();
            } catch (JMSException e) {
                logger.error("Could not close ActiveMQ session", e);
            }
        }
    }
}
