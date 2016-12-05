package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListPersistedImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>>>
        implements RealList<ELEMENT, RealListPersistedImpl<ELEMENT>> {

    private final Map<String, ELEMENT> elements;
    private final ExistingObjectFactory<ELEMENT> existingObjectHandler;

    private String name;
    private Connection connection;
    private Session session;
    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealListPersistedImpl(@Assisted Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted ExistingObjectFactory<ELEMENT> existingObjectHandler,
                                 ListenersFactory listenersFactory) {
        super(logger, false, new List.Data(id, name, description), listenersFactory);
        this.elements = Maps.newHashMap();
        this.existingObjectHandler = existingObjectHandler;
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(this, element);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.name = name;
        this.connection = connection;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        existingObjectReceiver = new JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(ChildUtil.name(name, "*") + "?consumer.retroactive=true")),
                Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = existingObjectHandler.create(logger, data);
                            if(element != null)
                                add(element);
                        }
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.connection = null;
        for(ELEMENT element : elements.values())
            element.uninit();
        if(existingObjectReceiver != null) {
            try {
                existingObjectReceiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close existing object receiver");
            }
            existingObjectReceiver = null;
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

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        if(connection != null) {
            try {
                element.init(ChildUtil.name(name, element.getId()), connection);
            } catch(JMSException e) {
                throw new HousemateException("Couldn't add element, failed to initialise it");
            }
        }
        for(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.get(id);
        if(element != null) {
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ELEMENT get(String name) {
        return elements.get(name);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<ELEMENT extends RealObject<?, ?>> {
        RealListPersistedImpl<ELEMENT> create(Logger logger,
                                              @Assisted("id") String id,
                                              @Assisted("name") String name,
                                              @Assisted("description") String description,
                                              ExistingObjectFactory<ELEMENT> existingObjectFactory);
    }

    /**
     * Created by tomc on 23/06/16.
     */
    public interface ExistingObjectFactory<ELEMENT extends RealObject<?, ?>> {
        ELEMENT create(Logger parentLogger, Object.Data data);
    }
}
