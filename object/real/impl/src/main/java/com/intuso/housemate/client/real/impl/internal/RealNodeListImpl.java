package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealNodeListImpl
        extends RealObject<List.Data, List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl>>
        implements RealList<ServerBaseNode<?, ?, ?, ?>, RealNodeListImpl> {

    private final Map<String, ServerBaseNode<?, ?, ?, ?>> elements;

    private String name;
    private Connection connection;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealNodeListImpl(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            ListenersFactory listenersFactory) {
        super(logger, false, new List.Data(id, name, description), listenersFactory);
        this.elements = Maps.newHashMap();
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ServerBaseNode<?, ?, ?, ?> node : this)
                listener.elementAdded(this, node);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.name = name;
        this.connection = connection;
        for(ServerBaseNode<?, ?, ?, ?> element : elements.values())
            element.init(ChildUtil.name(name, element.getId()), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.connection = null;
        for(ServerBaseNode<?, ?, ?, ?> element : elements.values())
            element.uninit();
    }

    @Override
    public void add(ServerBaseNode<?, ?, ?, ?> element) {
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
        for(List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ServerBaseNode<?, ?, ?, ?> remove(String id) {
        ServerBaseNode<?, ?, ?, ?> element = elements.get(id);
        if(element != null) {
            element.uninit();
            for (List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ServerBaseNode<?, ?, ?, ?> get(String name) {
        return elements.get(name);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ServerBaseNode<?, ?, ?, ?>> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<ELEMENT extends RealObject<?, ?>> {
        RealNodeListImpl create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                Iterable<? extends ELEMENT> elements);
    }
}
