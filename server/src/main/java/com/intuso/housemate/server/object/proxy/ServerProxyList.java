package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectFactory;
import com.intuso.utilities.object.ObjectListener;

import java.util.Iterator;
import java.util.Map;

/**
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 */
public class ServerProxyList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ServerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends ServerProxyObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ServerProxyList<CHILD_DATA, CHILD>, List.Listener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyList(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted ListData<CHILD_DATA> data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        return result;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof ListData)
            for(Map.Entry<String, ? extends HousemateData<?>> entry : data.getChildData().entrySet())
                getChild(entry.getKey()).copyValues(entry.getValue());
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super CHILD> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(CHILD element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    public final CHILD get(String name) {
        return getChild(name);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        for(List.Listener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        for(List.Listener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // do nothing
    }
}
