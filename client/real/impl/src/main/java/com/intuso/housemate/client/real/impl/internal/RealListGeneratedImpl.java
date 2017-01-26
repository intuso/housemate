package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListGeneratedImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>>>
        implements RealList<ELEMENT, RealListGeneratedImpl<ELEMENT>> {

    private final Map<String, ELEMENT> elements;

    private String name;
    private Connection connection;

    /**
     * @param logger {@inheritDoc}
     * @param elements the list's elements
     * @param managedCollectionFactory
     */
    @Inject
    public RealListGeneratedImpl(@Assisted Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted Iterable<? extends ELEMENT> elements,
                                 ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory);
        this.elements = Maps.newHashMap();
        for(ELEMENT element : elements)
            this.elements.put(element.getId(), element);
    }

    @Override
    public MemberRegistration addObjectListener(List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener, boolean callForExistingElements) {
        MemberRegistration listenerRegistration = super.addObjectListener(listener);
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
        for(ELEMENT element : elements.values())
            element.init(ChildUtil.name(name, element.getId()), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.connection = null;
        for(ELEMENT element : elements.values())
            element.uninit();
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
        for(List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.get(id);
        if(element != null) {
            // todo delete the element's queues/topics
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
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

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<ELEMENT extends RealObject<?, ?>> {
        RealListGeneratedImpl<ELEMENT> create(Logger logger,
                                          @Assisted("id") String id,
                                          @Assisted("name") String name,
                                          @Assisted("description") String description,
                                          Iterable<? extends ELEMENT> elements);
    }
}
