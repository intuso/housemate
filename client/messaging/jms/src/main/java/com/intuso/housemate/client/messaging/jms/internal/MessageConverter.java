package com.intuso.housemate.client.messaging.jms.internal;

import com.google.inject.Inject;
import com.intuso.housemate.client.messaging.api.internal.MessagingException;
import com.intuso.housemate.client.serialisation.javabin.internal.JavabinSerialiser;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by tomc on 02/03/17.
 */
public interface MessageConverter {

    Message toMessage(Session session, Serializable object) throws JMSException;
    <T extends Serializable> T fromMessage(Message message, Class<T> tClass);

    class Javabin implements MessageConverter {

        private final JavabinSerialiser javabinSerialiser;

        @Inject
        public Javabin(JavabinSerialiser javabinSerialiser) {
            this.javabinSerialiser = javabinSerialiser;
        }

        @Override
        public Message toMessage(Session session, Serializable object) throws JMSException {
            StreamMessage message = session.createStreamMessage();
            message.writeBytes(javabinSerialiser.serialise(object));
            return message;
        }

        @Override
        public <T extends Serializable> T fromMessage(Message message, Class<T> tClass) {
            String destination = null;
            try {
                destination = message != null && message.getJMSDestination() != null ? message.getJMSDestination().toString() : "unknown";
                if (message instanceof StreamMessage) {
                    StreamMessage streamMessage = (StreamMessage) message;
                    Object messageObject = streamMessage.readObject();
                    if (messageObject instanceof byte[])
                        return javabinSerialiser.deserialise((byte[]) messageObject, tClass);
                    else
                        throw new MessagingException("Received message data on " + destination + " that was not a " + byte[].class.getName());
                } else
                    throw new MessagingException("Received message on " + destination + " that wasn't a " + StreamMessage.class.getName() + " but a " + message.getClass().getName());
            } catch (JMSException e) {
                if(destination == null)
                    throw new MessagingException("Could not get destination from message", e);
                else
                    throw new MessagingException("Could not read object from message received on " + destination, e);
            }
        }
    }
}
