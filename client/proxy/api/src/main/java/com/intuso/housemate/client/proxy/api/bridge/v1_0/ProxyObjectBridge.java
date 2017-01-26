package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public abstract class ProxyObjectBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected VERSION_DATA data;
    protected final Class<INTERNAL_DATA> internalDataClass;
    protected final ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper;
    protected final ManagedCollection<LISTENER> listeners;
    private JMSUtil.Sender sender;
    private com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver<INTERNAL_DATA> receiver;

    protected ProxyObjectBridge(Logger logger, Class<INTERNAL_DATA> internalDataClass, ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper, ManagedCollectionFactory managedCollectionFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.internalDataClass = internalDataClass;
        this.dataMapper = dataMapper;
        this.listeners = managedCollectionFactory.create();
    }

    public final void init(String versionName, String internalName, Connection connection) throws JMSException {
        logger.debug("Init {} -> {}", internalName, versionName);
        sender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Topic, versionName);
        receiver = new com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver<>(logger, connection, com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Type.Topic, internalName, internalDataClass,
                new com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver.Listener<INTERNAL_DATA>() {
                    @Override
                    public void onMessage(INTERNAL_DATA data, boolean wasPersisted) {
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
            sender.close();
            sender = null;
        }
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    @Override
    public String getObjectClass() {
        return data.getObjectClass();
    }

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
    public MemberRegistration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    public interface Factory<OBJECT extends ProxyObjectBridge<?, ?, ?>> {
        OBJECT create(Logger logger);
    }
}
