package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.ListMapper;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by tomc on 28/11/16.
 */
public class RealListBridge<ELEMENT extends RealObjectBridge<?, ?, ?>>
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.List.Data, List.Data, List.Listener<? super ELEMENT, ? super RealListBridge<ELEMENT>>>
        implements List<ELEMENT, RealListBridge<ELEMENT>> {

    private final Map<String, ELEMENT> children = Maps.newHashMap();
    private final RealObjectBridge.Factory<ELEMENT> elementFactory;

    @Inject
    protected RealListBridge(@Assisted Logger logger,
                             ListMapper listMapper,
                             RealObjectBridge.Factory<ELEMENT> elementFactory,
                             ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.List.Data.class, listMapper, listenersFactory);
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
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT, ? super RealListBridge<ELEMENT>> listener, boolean callForExistingElements) {
        for(ELEMENT element : children.values())
            listener.elementAdded(this, element);
        return this.addObjectListener(listener);
    }
}
