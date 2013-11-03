package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.object.ObjectListener;

import java.util.Iterator;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child's resources
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <LIST> the type of the list
 */
public abstract class ProxyList<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>, ?>,
            CHILD_RESOURCES extends ProxyResources<?, ?>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            LIST extends ProxyList<RESOURCES, CHILD_RESOURCES, CHILD_DATA, CHILD, LIST>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, ListData<CHILD_DATA>, CHILD_DATA, CHILD, LIST, ListListener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyList(RESOURCES resources, CHILD_RESOURCES childResources, ListData data) {
        super(resources, childResources, data);
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
    protected java.util.List registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Receiver<HousemateData>() {
            @Override
            public void messageReceived(Message<HousemateData> message) throws HousemateException {
                CHILD child;
                try {
                    child = getResources().getObjectFactory().create(getSubResources(), (CHILD_DATA)message.getPayload());
                } catch(HousemateException e) {
                    throw new HousemateException("Could not create new list element", e);
                }
                child.init(ProxyList.this);
                addChild(child);
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Receiver<HousemateData>() {
            @Override
            public void messageReceived(Message<HousemateData> message) throws HousemateException {
                CHILD child = getChild(message.getPayload().getId());
                if(child != null) {
                    child.uninit();
                    removeChild(child.getId());
                }
            }
        }));
        return result;
    }

    @Override
    public final CHILD get(String id) {
        return getChild(id);
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
        super.childObjectAdded(childId, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        super.childObjectRemoved(childId, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }
}
