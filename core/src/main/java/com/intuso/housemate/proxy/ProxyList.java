package com.intuso.housemate.proxy;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.comms.Receiver;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.list.ListListener;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.listeners.ListenerRegistration;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/07/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyList<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, SWBL, SWR>>,
            SR extends ProxyResources<?>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>,
            L extends ProxyList<R, SR, SWBL, SWR, L>>
        extends ProxyObject<R, SR, ListWrappable<SWBL>, SWBL, SWR, L, ListListener<? super SWR>>
        implements List<SWR> {

    public ProxyList(R resources, SR subResources, ListWrappable listWrappable) {
        super(resources, subResources, listWrappable);
    }

    @Override
    public ListenerRegistration<? super ListListener<? super SWR>> addObjectListener(ListListener<? super SWR> listener, boolean callForExistingElements) {
        ListenerRegistration<? super ListListener<? super SWR>> listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(SWR element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List<ListenerRegistration<?>> registerListeners() {
        java.util.List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addMessageListener(ADD, new Receiver<HousemateObjectWrappable>() {
            @Override
            public void messageReceived(Message<HousemateObjectWrappable> message) throws HousemateException {
                SWR wrapper;
                try {
                    wrapper = getResources().getObjectFactory().create(getSubResources(), (SWBL)message.getPayload());
                } catch(HousemateException e) {
                    throw new HousemateException("Could not create new list element", e);
                }
                wrapper.init(ProxyList.this);
                addWrapper(wrapper);
                for(ListListener<? super SWR> listener : getObjectListeners())
                    listener.elementAdded(wrapper);
            }
        }));
        result.add(addMessageListener(REMOVE, new Receiver<HousemateObjectWrappable>() {
            @Override
            public void messageReceived(Message<HousemateObjectWrappable> message) throws HousemateException {
                SWR wrapper = removeWrapper(message.getPayload().getId());
                if(wrapper != null) {
                    wrapper.uninit();
                    for(ListListener<? super SWR> listener : getObjectListeners())
                        listener.elementRemoved(wrapper);
                }
            }
        }));
        return result;
    }

    @Override
    public final SWR get(String name) {
        return (SWR)getWrapper(name);
    }

    @Override
    public int size() {
        return getWrappers().size();
    }

    @Override
    public Iterator<SWR> iterator() {
        return getWrappers().iterator();
    }
}
