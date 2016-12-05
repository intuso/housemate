package com.intuso.housemate.client.proxy.api.internal.object;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * @param <ELEMENT> the type of the child
 */
public abstract class ProxyList<ELEMENT extends ProxyObject<?, ?>, LIST extends ProxyList<ELEMENT, ?>>
        extends ProxyObject<List.Data, List.Listener<? super ELEMENT, ? super LIST>>
        implements List<ELEMENT, LIST> {

    private final Map<String, ELEMENT> children = Maps.newHashMap();
    private final ProxyObject.Factory<ELEMENT> elementFactory;

    /**
     * @param logger {@inheritDoc}
     * @param elementFactory
     */
    public ProxyList(Logger logger, ListenersFactory listenersFactory, Factory<ELEMENT> elementFactory) {
        super(logger, List.Data.class, listenersFactory);
        this.elementFactory = elementFactory;
    }

    // NB no need to init children, as they won't exist before we've init'ed and connected

    @Override
    protected void uninitChildren() {
        for(ELEMENT ELEMENT : children.values())
            ELEMENT.uninit();
    }

    @Override
    public final ELEMENT get(String id) {
        return children.get(id);
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return children.values().iterator();
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements) {
        for(ELEMENT element : children.values())
            listener.elementAdded((LIST) this, element);
        return this.addObjectListener(listener);
    }
}
