package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
            DATA extends Object.Data,
            LISTENER extends Object.Listener> implements Object<LISTENER> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    protected final ManagedCollection<LISTENER> listeners;
    private final Class<DATA> dataClass;

    protected DATA data = null;
    private JMSUtil.Receiver<DATA> receiver;

    /**
     * @param logger the log
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.dataClass = dataClass;
        this.listeners = managedCollectionFactory.create();
    }

    @Override
    public String getObjectClass() {
        return data == null ? null : data.getObjectClass();
    }

    @Override
    public String getId() {
        return data == null ? null : data.getId();
    }

    @Override
    public String getName() {
        return data == null ? null : data.getName();
    }

    @Override
    public String getDescription() {
        return data == null ? null : data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    protected final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init {}", name);
        receiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, name, dataClass,
                new JMSUtil.Receiver.Listener<DATA>() {
                    @Override
                    public void onMessage(DATA data, boolean wasPersisted) {
                        ProxyObject.this.data = data;
                        dataUpdated();
                    }
                });
        initChildren(name, connection);
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

    protected final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    protected void dataUpdated() {}

    public boolean isLoaded() {
        return data != null;
    }

    public abstract ProxyObject<?, ?> getChild(String id);

    public interface Factory<OBJECT extends ProxyObject<?, ?>> {
        OBJECT create(Logger logger);
    }
}
