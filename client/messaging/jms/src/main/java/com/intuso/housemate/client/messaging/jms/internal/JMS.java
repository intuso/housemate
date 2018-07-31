package com.intuso.housemate.client.messaging.jms.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.messaging.api.internal.MessageConstants;
import com.intuso.housemate.client.messaging.api.internal.MessagingException;
import org.slf4j.Logger;

import javax.jms.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tomc on 21/02/17.
 */
public class JMS {

    public final static String TYPE = "jms";

    public static class Sender implements com.intuso.housemate.client.messaging.api.internal.Sender {

        private final Logger logger;
        private final MessageConverter messageConverter;
        private final Session session;
        private final MessageProducer producer;

        @Inject
        public Sender(@Assisted Logger logger,
                      @Assisted String name,
                      MessageConverter messageConverter,
                      Connection connection) throws JMSException {
            this.logger = logger;
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.messageConverter = messageConverter;
            this.producer = session.createProducer(session.createTopic(name));
        }

        @Override
        public void close() {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    logger.error("Failed to close producer");
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    logger.error("Failed to close session");
                }
            }
        }

        @Override
        public void send(Serializable object, boolean persistent) {
            try {
                Message message = messageConverter.toMessage(session, object);
                if (persistent)
                    message.setBooleanProperty(MessageConstants.STORE, true);
                producer.send(message);
                logger.trace("Sent {} on {}", object, producer.getDestination());
            } catch (JMSException e) {
                throw new RuntimeException("Failed to send message", e);
            }
        }

        public static class FactoryImpl implements com.intuso.housemate.client.messaging.api.internal.Sender.Factory {

            private final MessageConverter messageConverter;
            private final Connection connection;

            public FactoryImpl(MessageConverter messageConverter, Connection connection) {
                this.messageConverter = messageConverter;
                this.connection = connection;
            }

            @Override
            public com.intuso.housemate.client.messaging.api.internal.Sender create(Logger logger, String name) {
                try {
                    return new Sender(logger, name, messageConverter, connection);
                } catch (JMSException e) {
                    throw new MessagingException("Failed to create JMS sender");
                }
            }
        }
    }

    /**
     * Created by tomc on 02/03/17.
     */
    public static class Receiver<T extends Serializable> implements com.intuso.housemate.client.messaging.api.internal.Receiver<T> {

        private final Logger logger;
        private final MessageConverter messageConverter;
        private final Session session;
        private final Destination destination;
        private final MessageConsumer consumer;
        private final Class<T> tClass;

        @Inject
        public Receiver(@Assisted Logger logger,
                        @Assisted String name,
                        @Assisted Class tClass,
                        @Assisted MessageConverter messageConverter,
                        Connection connection) throws JMSException {
            this.logger = logger;
            this.messageConverter = messageConverter;
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.destination = session.createTopic(name + "?consumer.retroactive=true");
            this.consumer = session.createConsumer(destination);
            this.tClass = tClass;
        }

        @Override
        public T getMessage() {
            try {
                logger.trace("Getting message from {}", destination);
                Message persisted = consumer.receiveNoWait();
                return persisted == null ? null : messageConverter.fromMessage(persisted, tClass);
            } catch (JMSException e) {
                throw new MessagingException("Failed to get message", e);
            }
        }

        @Override
        public Iterator<T> getMessages() {
            logger.trace("Getting messages from {}", destination);
            return new ConsumerIterator();
        }

        @Override
        public void listen(Listener<T> listener) {
            try {
                logger.trace("Listening for messages on {}", destination);
                MessageListener messageListener = new MessageListenerImpl(listener);
                Message message;
                while ((message = consumer.receiveNoWait()) != null)
                    messageListener.onMessage(message);
                consumer.setMessageListener(messageListener);
            } catch(JMSException e) {
                throw new MessagingException("Failed to listen for messages", e);
            }
        }

        @Override
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

            private final Listener<T> listener;

            private MessageListenerImpl(Listener<T> listener) {
                this.listener = listener;
            }

            @Override
            public void onMessage(Message message) {
                logger.trace("Received message on {}", destination);
                T object = messageConverter.fromMessage(message, tClass);
                if(object != null) {
                    logger.trace("Deserialised message on {} as {}", destination, object);
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

        private class ConsumerIterator implements Iterator<T> {

            private T next;

            public ConsumerIterator() {
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
                getNext();
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void getNext() {
                try {
                    Message persisted = consumer.receiveNoWait();
                    next = persisted == null ? null : messageConverter.fromMessage(persisted, tClass);
                    if(next != null)
                        logger.trace("Got {} from {}", next, destination);
                    else
                        logger.trace("No more messages on {}", destination);
                } catch(JMSException e) {
                    throw new MessagingException("Failed to get next persisted message", e);
                }
            }
        }

        public static class FactoryImpl implements com.intuso.housemate.client.messaging.api.internal.Receiver.Factory {

            private final MessageConverter messageConverter;
            private final Connection connection;

            public FactoryImpl(MessageConverter messageConverter, Connection connection) {
                this.messageConverter = messageConverter;
                this.connection = connection;
            }

            @Override
            public <T extends Serializable> com.intuso.housemate.client.messaging.api.internal.Receiver create(Logger logger, String name, Class<T> tClass) {
                try {
                    return new Receiver(logger, name, tClass, messageConverter, connection);
                } catch (JMSException e) {
                    throw new MessagingException("Failed to create JMS receiver");
                }
            }
        }
    }
}
