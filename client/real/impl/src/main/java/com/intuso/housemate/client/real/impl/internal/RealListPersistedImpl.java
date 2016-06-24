package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListPersistedImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>>>
        implements RealList<ELEMENT, RealListPersistedImpl<ELEMENT>>, MessageListener {

    private final Map<String, ELEMENT> elements;
    private final ExistingObjectFactory<ELEMENT> existingObjectHandler;

    private String name;
    private Connection connection;
    private Session session;
    private MessageConsumer existingObjectConsumer;

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
        super(logger, new List.Data(id, name, description), listenersFactory);
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
        existingObjectConsumer = session.createConsumer(session.createTopic(ChildUtil.name(name, "*")));
        existingObjectConsumer.setMessageListener(this);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.connection = null;
        for(ELEMENT element : elements.values())
            element.uninit();
        if(existingObjectConsumer != null) {
            try {
                existingObjectConsumer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform producer");
            }
            existingObjectConsumer = null;
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

    @Override
    public void onMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                java.lang.Object messageObject = streamMessage.readObject();
                if(messageObject instanceof byte[]) {
                    java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                    if (object instanceof Object.Data) {
                        // pass to the handler if the object doesn't already exist
                        Object.Data data = (Object.Data) object;
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = existingObjectHandler.create(logger, data);
                            if(element != null)
                                add(element);
                        }
                    } else
                        logger.warn("Deserialised message object that wasn't a {}", Object.Data.class.getName());
                } else
                    logger.warn("Message data was not a byte[]");
            } catch(JMSException e) {
                logger.error("Failed to read object from message", e);
            }
        } else
            logger.warn("Received message that wasn't a {}", StreamMessage.class.getName());
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
