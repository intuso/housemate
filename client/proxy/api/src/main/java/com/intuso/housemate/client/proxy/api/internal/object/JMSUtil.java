package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.MessageConstants;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import org.slf4j.Logger;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by tomc on 01/12/16.
 */
public class JMSUtil {

    public static class Sender {

        private final Session session;
        private final MessageProducer producer;

        public Sender(Session session, MessageProducer producer) {
            this.session = session;
            this.producer = producer;
        }

        public void close() throws JMSException {
            producer.close();
        }

        public void send(Serializable object, boolean persistent) throws JMSException {
            if(producer != null) {
                StreamMessage streamMessage = session.createStreamMessage();
                if(persistent)
                    streamMessage.setBooleanProperty(MessageConstants.STORE, true);
                streamMessage.writeBytes(Serialiser.serialise(object));
                producer.send(streamMessage);
            }
        }
    }

    public static class Receiver<OBJECT extends Serializable> {

        private final Logger logger;
        private final MessageConsumer consumer;
        private final Class<OBJECT> objectClass;
        private final Listener<OBJECT> listener;

        public Receiver(final Logger logger, final MessageConsumer consumer, Class<OBJECT> objectClass, Listener<OBJECT> listener) throws JMSException {
            this.logger = logger;
            this.consumer = consumer;
            this.objectClass = objectClass;
            this.listener = listener;

            // run in separate thread. Setting a message listener for ActiveMQ connections stops and starts the session if
            // it's already running. However, this causes deadlock if this thread is from a message being processed.
            // todo probably a better way of dealing with this issue!! maybe separate sessions for producer/consumer?
            new Thread() {
                @Override
                public void run() {
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
            }.start();
        }

        public void close() throws JMSException {
            consumer.close();
        }

        private class MessageListenerImpl implements MessageListener {

            @Override
            public void onMessage(Message message) {
                if (message instanceof StreamMessage) {
                    StreamMessage streamMessage = (StreamMessage) message;
                    try {
                        java.lang.Object messageObject = streamMessage.readObject();
                        if (messageObject instanceof byte[]) {
                            java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                            if (objectClass.isAssignableFrom(object.getClass()))
                                listener.onMessage((OBJECT) object, message.getBooleanProperty(MessageConstants.STORE));
                            else
                                logger.warn("Deserialised message object that wasn't a {}", objectClass.getName());
                        } else
                            logger.warn("Message data was not a {}", byte[].class.getName());
                    } catch (JMSException e) {
                        logger.error("Could not read object from received message", e);
                    }
                } else
                    logger.error("Received message that wasn't a {}", StreamMessage.class.getName());
            }
        }

        public interface Listener<OBJECT extends Serializable> {
            void onMessage(OBJECT object, boolean wasPersisted);
        }
    }
}
