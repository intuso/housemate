package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends com.intuso.housemate.client.api.internal.object.Object.Listener>
        implements Object<LISTENER> {

    public final static String REAL = "real";

    protected final Logger logger;
    protected final DATA data;
    protected final Listeners<LISTENER> listeners;

    private JMSUtil.Sender sender;

    protected RealObject(Logger logger, DATA data, ListenersFactory listenersFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.data = data;
        this.listeners = listenersFactory.create();
    }

    public final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init {}", name);
        sender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Topic, name);
        sendData();
        initChildren(name, connection);
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(sender != null) {
            sender.close();
            sender = null;
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
                sender.send(data, true);
            } catch (JMSException e) {
                logger.error("Failed to send data object", e);
            }
        }
    }
}
