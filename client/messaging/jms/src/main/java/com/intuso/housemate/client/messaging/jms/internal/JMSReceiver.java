package com.intuso.housemate.client.messaging.jms.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.messaging.api.internal.MessageConstants;
import com.intuso.housemate.client.messaging.api.internal.MessagingException;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Type;
import org.slf4j.Logger;

import javax.jms.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tomc on 02/03/17.
 */
public class JMSReceiver<T extends Serializable> implements Receiver<T> {

    private final Logger logger;
    private final MessageConverter messageConverter;
    private final Destination destination;
    private final Session session;
    private final MessageConsumer consumer;
    private final Class<T> tClass;

    @Inject
    public JMSReceiver(@Assisted Logger logger,
                       @Assisted Type type,
                       @Assisted String name,
                       @Assisted Class<T> tClass,
                       MessageConverter messageConverter,
                       Connection connection) throws JMSException {
        this.logger = logger;
        this.messageConverter = messageConverter;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        switch (type) {
            case Queue:
                this.destination = session.createQueue(name);
                break;
            case Topic:
                this.destination = session.createTopic(name + "?consumer.retroactive=true");
                break;
            default:
                throw new MessagingException("Unknown type " + type);
        }
        this.consumer = session.createConsumer(destination);
        this.tClass = tClass;
    }

    @Override
    public T getPersistedMessage() {
        try {
            Message persisted = consumer.receiveNoWait();
            return persisted == null ? null : messageConverter.fromMessage(persisted, tClass);
        } catch (JMSException e) {
            throw new MessagingException("Failed to get message", e);
        }
    }

    @Override
    public Iterator<T> getPersistedMessages() {
        return new ConsumerIterator();
    }

    @Override
    public void listen(Listener<T> listener) {
        try {
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
            } catch(JMSException e) {
                throw new MessagingException("Failed to get next persisted message", e);
            }
        }
    }

    public interface Factory {
        Receiver<?> create(Logger logger, Type type, String name, Class tClass);
    }

    public class FactoryImpl implements Receiver.Factory {

        private final Factory factory;

        @Inject
        public FactoryImpl(Factory factory) {
            this.factory = factory;
        }

        @Override
        public <T extends Serializable> Receiver<T> create(Logger logger, Type type, String name, Class<T> tClass) {
            return (Receiver<T>) factory.create(logger, type, name, tClass);
        }
    }
}
