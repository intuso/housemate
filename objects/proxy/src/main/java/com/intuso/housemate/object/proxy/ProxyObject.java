package com.intuso.housemate.object.proxy;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.ChildData;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectFactory;
import com.intuso.utilities.object.ObjectListener;

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
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>, ?>,
            CHILD_RESOURCES extends ProxyResources<?, ?>,
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends ProxyObject<?, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends com.intuso.housemate.api.object.ObjectListener>
        extends HousemateObject<RESOURCES, DATA, CHILD_DATA, CHILD, LISTENER>
        implements ObjectListener<CHILD> {

    private Joiner PATH_JOINER = Joiner.on("/");

    private ProxyRoot<?, ?, ?, ?, ?, ?> proxyRoot;
    private final CHILD_RESOURCES childResources;
    private final Map<String, LoadManager> pendingLoads = Maps.newHashMap();
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
        result.add(addChildListener(this));
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
                LoadManager manager = pendingLoads.get(message.getPayload().getLoaderName());
                if(manager != null) {
                    if(message.getPayload().getError() != null) {
                        getLog().e("Failed to load " + message.getPayload().getLoaderName() + " tree " + message.getPayload().getTreeData().getId() + ". " + message.getPayload().getError());
                        manager.responseReceived(message.getPayload().getTreeData().getId(), false);
                    } else {
                        try {
                            if(message.getPayload().getTreeData().getData() != null) {
                                CHILD object = createObject(message.getPayload().getTreeData());
                                object.init(ProxyObject.this);
                                addChild(object);
                            }
                            manager.responseReceived(message.getPayload().getTreeData().getId(), true);
                        } catch(HousemateException e) {
                            getLog().e("Failed to unwrap load response");
                            getLog().st(e);
                            manager.responseReceived(message.getPayload().getTreeData().getId(), true);
                        }
                    }
                }
            }
        }));
        return result;
    }

    protected CHILD createObject(TreeData<CHILD_DATA> treeData) throws HousemateException {
        CHILD object = getResources().getObjectFactory().create(getSubResources(), treeData.getData());
        getLog().d("Created " + object.getId());
        object.initObject(treeData);
        return object;
    }

    protected void initObject(TreeData<?> treeData) throws HousemateException {
        childData.putAll(treeData.getChildData());
        for(TreeData<?> childData : treeData.getChildren().values())
            addChild(createObject((TreeData<CHILD_DATA>) childData));
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
            createChildren(new ObjectFactory<CHILD_DATA, CHILD, HousemateException>() {
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
        CHILD object = getChild(childId);
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
        else if(manager.getToLoad().size() == 0)
            manager.allLoaded();
        else {
            pendingLoads.put(manager.getName(), manager);
            for(TreeLoadInfo tree : manager.getToLoad().values()) {
                checkTree(tree);
                sendMessage(HousemateObject.LOAD_REQUEST, new LoadRequest(manager.getName(), tree));
            }
        }
    }

    protected void checkTree(TreeLoadInfo tree) {
        CHILD child = getChild(tree.getId());
        tree.setLoad(child == null);
        if(child != null)
            for(TreeLoadInfo childTree : tree.getChildren().values())
                child.checkTree(childTree);
    }

    /**
     * Gets this object as its sub class type
     * @return this object as its sub class type
     */
    protected final OBJECT getThis() {
        return (OBJECT)this;
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>> listeners = childLoadedListeners.get(child.getId());
        if(listeners != null) {
            for(ChildLoadedListener<? super OBJECT, ? super CHILD> listener : listeners)
                listener.childLoaded(getThis(), child);
        }
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        // do nothing
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
