package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends com.intuso.housemate.client.api.internal.object.Object.Listener>
        implements Object<LISTENER> {

    public final static String REAL = "real";

    protected final Logger logger;
    protected final boolean persistent;
    protected final DATA data;
    protected final Listeners<LISTENER> listeners;
    private Session session;
    private JMSUtil.Sender sender;

    protected RealObject(Logger logger, boolean persistent, DATA data, ListenersFactory listenersFactory) {
        this.persistent = persistent;
        logger.debug("Creating");
        this.logger = logger;
        this.data = data;
        this.listeners = listenersFactory.create();
    }

    public final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init");
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = new JMSUtil.Sender(session, session.createProducer(session.createTopic(name)));
        initChildren(name, connection);
        sendData();
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(sender != null) {
            try {
                sender.close();
            } catch (JMSException e) {
                logger.error("Failed to close sender");
            }
            sender = null;
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
        if(sender != null) {
            try {
                sender.send(data, persistent);
            } catch (JMSException e) {
                logger.error("Failed to send data object", e);
            }
        }
    }
}
