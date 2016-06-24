package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends com.intuso.housemate.client.api.internal.object.Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected final DATA data;
    protected final Listeners<LISTENER> listeners;
    private Session session;
    private MessageProducer producer;

    protected RealObject(Logger logger, DATA data, ListenersFactory listenersFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.data = data;
        this.listeners = listenersFactory.create();
    }

    public final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init");
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(session.createTopic(name));
        initChildren(name, connection);
        sendData();
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                logger.error("Failed to close producer");
            }
            producer = null;
        }
        if(session != null) {
            try {
                session.close();
            } catch(JMSException e) {
                logger.error("Failed to close session");
            }
            session = null;
        }
    }

    protected void uninitChildren() {}

    @Override
    public final String getId() {
        return data.getId();
    }

    @Override
    public final String getName() {
        return data.getName();
    }

    @Override
    public final String getDescription() {
        return data.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(LISTENER listener) {
        return listeners.addListener(listener);
    }

    protected final DATA getData() {
        return data;
    }

    protected final void sendData() {
        if(producer != null) {
            try {
                StreamMessage streamMessage = session.createStreamMessage();
                streamMessage.writeObject(Serialiser.serialise(data));
                producer.send(streamMessage);
            } catch (JMSException e) {
                logger.error("Failed to send data object", e);
            }
        }
    }
}
