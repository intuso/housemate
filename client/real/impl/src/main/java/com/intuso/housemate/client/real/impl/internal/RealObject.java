package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected final DATA data;
    protected final Listeners<LISTENER> listeners;
    private Session session;
    private MessageProducer producer;

    protected RealObject(Logger logger, DATA data, ListenersFactory listenersFactory) {
        this.logger = logger;
        this.data = data;
        this.listeners = listenersFactory.create();
    }

    public final void init(String name, Session session) throws JMSException {
        this.session = session;
        producer = session.createProducer(session.createTopic(name));
        sendData();
        initChildren(name, session);
    }

    protected void initChildren(String name, Session session) throws JMSException {}

    public final void uninit() {
        uninitChildren();
        if(producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                logger.error("Failed to close producer");
            }
            producer = null;
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
