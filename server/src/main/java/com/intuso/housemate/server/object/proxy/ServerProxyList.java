package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
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
        extends ServerProxyObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ServerProxyList<CHILD_DATA, CHILD>, ListListener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyList(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted ListData<CHILD_DATA> data) {
        super(log, listenersFactory, injector, data);
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
    public ListenerRegistration addObjectListener(ListListener<? super CHILD> listener, boolean callForExistingElements) {
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
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }
}
