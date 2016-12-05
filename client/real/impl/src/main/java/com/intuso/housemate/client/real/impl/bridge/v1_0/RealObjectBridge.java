package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.ObjectMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.real.impl.internal.JMSUtil;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public abstract class RealObjectBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected VERSION_DATA data;
    protected final Class<VERSION_DATA> versionDataClass;
    protected final ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper;
    protected final Listeners<LISTENER> listeners;
    private Session session;
    private JMSUtil.Sender sender;
    private com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<VERSION_DATA> receiver;

    protected RealObjectBridge(Logger logger, Class<VERSION_DATA> versionDataClass, ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper, ListenersFactory listenersFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.versionDataClass = versionDataClass;
        this.dataMapper = dataMapper;
        this.listeners = listenersFactory.create();
    }

    public final void init(String versionName, String internalName, Connection connection) throws JMSException {
        logger.debug("Init");
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = new JMSUtil.Sender(session, session.createProducer(session.createTopic(internalName)));
        receiver = new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(versionName)),
                versionDataClass,
                new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver.Listener<VERSION_DATA>() {
                    @Override
                    public void onMessage(VERSION_DATA data, boolean wasPersisted) {
                        try {
                            sender.send(dataMapper.map(data), wasPersisted);
                        } catch (JMSException e) {
                            logger.error("Failed to send data object", e);
                        }
                    }
                });
        initChildren(versionName, internalName, connection);
    }

    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {}

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
        if(receiver != null) {
            try {
                receiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close receiver");
            }
            receiver = null;
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
    public String getId() {
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

    public interface Factory<OBJECT extends RealObjectBridge<?, ?, ?>> {
        OBJECT create(Logger logger);
    }
}
