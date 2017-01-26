package com.intuso.housemate.client.proxy.api.internal.object;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <ELEMENT> the type of the child
 */
public abstract class ProxyList<ELEMENT extends ProxyObject<?, ?>, LIST extends ProxyList<ELEMENT, ?>>
        extends ProxyObject<List.Data, List.Listener<? super ELEMENT, ? super LIST>>
        implements List<ELEMENT, LIST> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final Map<String, java.lang.Object> newElementLocks = Maps.newHashMap();
    private final ProxyObject.Factory<ELEMENT> elementFactory;

    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param elementFactory
     */
    public ProxyList(Logger logger, ManagedCollectionFactory managedCollectionFactory, Factory<ELEMENT> elementFactory) {
        super(logger, List.Data.class, managedCollectionFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String name, final Connection connection) throws JMSException {
        super.initChildren(name, connection);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, "*"), Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if (!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if (element != null) {
                                elements.put(data.getId(), element);
                                synchronized (newElementLocks) {
                                    java.lang.Object idLock = newElementLocks.remove(data.getId());
                                    if(idLock != null) {
                                        synchronized (idLock) {
                                            idLock.notifyAll();
                                        }
                                    }
                                }
                                try {
                                    element.init(ChildUtil.name(name, data.getId()), connection);
                                } catch (JMSException e) {
                                    logger.error("Failed to init child {}", data.getId(), e);
                                }
                                for (List.Listener<? super ELEMENT, ? super LIST> listener : listeners)
                                    listener.elementAdded((LIST) ProxyList.this, element);
                            }
                        }
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        synchronized (elements) {
            for (ELEMENT ELEMENT : elements.values())
                ELEMENT.uninit();
            if (existingObjectReceiver != null) {
                existingObjectReceiver.close();
                existingObjectReceiver = null;
            }
        }
    }

    @Override
    public final ELEMENT get(String id) {
        return elements.get(id);
    }

    @Override
    public ELEMENT getByName(String name) {
        for (ELEMENT element : this)
            if (name.equalsIgnoreCase(element.getName()))
                return element;
        return null;
    }

    public final ELEMENT getWhenLoaded(String id) throws InterruptedException {
        return getWhenLoaded(id, 1000);
    }

    public final ELEMENT getWhenLoaded(String id, long timeout) throws InterruptedException {

        if(elements.containsKey(id))
            return elements.get(id);

        // get/create a lock to wait on for the object to be created
        java.lang.Object idLock = newElementLocks.get(id);
        if(idLock == null) {
            synchronized (newElementLocks) {

                // check again in case created since last check
                if(elements.containsKey(id))
                    return elements.get(id);

                idLock = newElementLocks.get(id);
                // check again in case created before we got lock
                if(idLock == null) {
                    idLock = new java.lang.Object();
                    newElementLocks.put(id, idLock);
                }
            }
        }
        synchronized (idLock) {
            // check again in case created since last check
            if(elements.containsKey(id))
                return elements.get(id);

            idLock.wait(timeout);
        }

        return elements.get(id);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    @Override
    public MemberRegistration addObjectListener(List.Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded((LIST) this, element);
        return this.addObjectListener(listener);
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        return get(id);
    }
}
