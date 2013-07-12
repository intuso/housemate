package com.intuso.housemate.object.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.ChildData;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.wrapper.Wrapper;
import com.intuso.utilities.wrapper.WrapperFactory;
import com.intuso.utilities.wrapper.WrapperListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <OBJECT> the type of the object
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends ProxyObject<?, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends ObjectListener>
        extends HousemateObject<RESOURCES, DATA, CHILD_DATA, CHILD, LISTENER>
        implements BaseObject<LISTENER>, WrapperListener<CHILD> {

    private ProxyRoot<?, ?, ?, ?, ?, ?> proxyRoot;
    private final CHILD_RESOURCES childResources;
    private final Map<String, Set<LoadManager>> pendingLoads = Maps.newHashMap();
    private final Map<String, ChildData> childData = Maps.newHashMap();
    private final Listeners<AvailableChildrenListener<? super OBJECT>> availableChildrenListeners = new Listeners<AvailableChildrenListener<? super OBJECT>>();
    private final Map<String, Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>>> childLoadedListeners = Maps.newHashMap();

    /**
     * @param resources the resources
     * @param childResources the child resources
     * @param data the data object
     */
    protected ProxyObject(RESOURCES resources, CHILD_RESOURCES childResources, DATA data) {
        super(resources, data);
        this.childResources = childResources;
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = Lists.newArrayList();
        result.add(addWrapperListener(this));
        result.add(addMessageListener(CHILD_ADDED, new Receiver<ChildData>() {
            @Override
            public void messageReceived(Message<ChildData> message) throws HousemateException {
                ChildData cd = message.getPayload();
                if(childData.get(cd.getId()) == null) {
                    childData.put(cd.getId(), cd);
                    for(AvailableChildrenListener<? super OBJECT> listener : availableChildrenListeners)
                        listener.childAdded(getThis(), cd);
                }
            }
        }));
        result.add(addMessageListener(CHILD_REMOVED, new Receiver<ChildData>() {
            @Override
            public void messageReceived(Message<ChildData> message) throws HousemateException {
                ChildData cd = message.getPayload();
                childData.remove(cd.getId());
                for(AvailableChildrenListener<? super OBJECT> listener : availableChildrenListeners)
                    listener.childRemoved(getThis(), cd);
            }
        }));
        result.add(addMessageListener(LOAD_RESPONSE, new Receiver<LoadResponse<CHILD_DATA>>() {
            @Override
            public void messageReceived(Message<LoadResponse<CHILD_DATA>> message) throws HousemateException {
                String id = message.getPayload().getChildId();
                if(message.getPayload().getError() != null) {
                    getLog().e("Failed to load " + id + ". " + message.getPayload().getError());
                    if(pendingLoads.get(id) != null) {
                        for(LoadManager manager : pendingLoads.get(id)) {
                            manager.failed(id);
                            manager.finished(id);
                        }
                    }
                } else {
                    try {
                        CHILD object = getResources().getObjectFactory().create(getSubResources(), message.getPayload().getData());
                        for(ChildData cd : message.getPayload().getChildData())
                            ((ProxyObject)object).childData.put(cd.getId(), cd);
                        object.init(ProxyObject.this);
                        addWrapper(object);
                        if(pendingLoads.get(object.getId()) != null) {
                            for(LoadManager manager : pendingLoads.get(object.getId()))
                                manager.finished(object.getId());
                        }
                    } catch(HousemateException e) {
                        getLog().e("Failed to unwrap load response");
                        getLog().st(e);
                        if(pendingLoads.get(id) != null) {
                            for(LoadManager manager : pendingLoads.get(id)) {
                                manager.failed(id);
                                manager.finished(id);
                            }
                        }
                    }
                }
            }
        }));
        return result;
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {

        // get the broker for this object
        if(this instanceof ProxyRoot)
            proxyRoot = (ProxyRoot)this;
        else if(parent != null && parent instanceof ProxyObject)
            proxyRoot = ((ProxyObject)parent).proxyRoot;

        // unwrap children
        try {
            unwrapChildren(new WrapperFactory<CHILD_DATA, CHILD, HousemateException>() {
                @Override
                public CHILD create(CHILD_DATA data) throws HousemateException {
                    return getResources().getObjectFactory().create(getSubResources(), data);
                }
            });
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to unwrap child object", e);
        }
    }

    @Override
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {}

    /**
     * Sends a message to the broker
     * @param type the type of the message to send
     * @param payload the message payload
     * @param <MV> the type of the message payload
     */
    protected final <MV extends Message.Payload> void sendMessage(String type, MV payload) {
        proxyRoot.sendMessage(new Message<MV>(getPath(), type, payload));
    }

    /**
     * Gets the root object
     * @return the root object
     */
    protected ProxyRoot<?, ?, ?, ?, ?, ?> getProxyRoot() {
        return proxyRoot;
    }

    /**
     * Gets the child resources
     * @return the child resources
     */
    protected CHILD_RESOURCES getSubResources() {
        return childResources;
    }

    /**
     * Gets the names of the child objects
     * @return the names of the child objects
     */
    protected Set<String> getChildNames() {
        return getData().getChildData().keySet();
    }

    public ListenerRegistration addAvailableChildrenListener(AvailableChildrenListener<? super OBJECT> listener, boolean callForExisting) {
        ListenerRegistration result = availableChildrenListeners.addListener(listener);
        if(callForExisting)
            for(ChildData cd : childData.values())
                listener.childAdded(getThis(), cd);
        return result;
    }

    public void addChildLoadedListener(String childId, ChildLoadedListener<? super OBJECT, ? super CHILD> listener) {
        CHILD object = getWrapper(childId);
        if(object != null)
            listener.childLoaded(getThis(), object);
        else {
            Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>> listeners = childLoadedListeners.get(childId);
            if(listeners == null) {
                listeners = new Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>>();
                childLoadedListeners.put(childId, listeners);
            }
            listeners.addListener(listener);
        }
    }

    /**
     * Makes a request to load the child object for the given id
     * @param manager the load manager used to specify what to load and notify about failures or when objects are loaded
     */
    public void load(LoadManager manager) {
        if(manager == null)
            throw new HousemateRuntimeException("Null manager");
        else {
            for(String id : manager.getToLoad()) {
                if(getWrapper(id) != null)
                    manager.finished(id);
                else {
                    if(pendingLoads.get(id) != null)
                        pendingLoads.get(id).add(manager);
                    else {
                        pendingLoads.put(id, Sets.<LoadManager>newHashSet());
                        pendingLoads.get(id).add(manager);
                        sendMessage(LOAD_REQUEST, new LoadRequest(id));
                    }
                }
            }
        }
    }

    /**
     * Gets this object as its sub class type
     * @return this object as its sub class type
     */
    protected final OBJECT getThis() {
        return (OBJECT)this;
    }

    @Override
    public void childWrapperAdded(String childId, CHILD wrapper) {
        Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>> listeners = childLoadedListeners.get(wrapper.getId());
        if(listeners != null) {
            for(ChildLoadedListener<? super OBJECT, ? super CHILD> listener : listeners)
                listener.childLoaded(getThis(), wrapper);
        }
    }

    @Override
    public void childWrapperRemoved(String childId, CHILD wrapper) {
        // do nothing
    }

    @Override
    public void ancestorAdded(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        // do nothing
    }

    @Override
    public void ancestorRemoved(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        // do nothing
    }
}
