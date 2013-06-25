package com.intuso.housemate.object.proxy;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionManager;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.ConnectionStatusChangeListener;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.ReconnectResponse;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.housemate.api.object.root.proxy.ProxyRootListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.wrapper.Wrapper;
import com.intuso.utilities.wrapper.WrapperListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <USER> the type of the users
 * @param <USERS> the type of the users list
 * @param <TYPE> the type of the types
 * @param <TYPES> the type of the types list
 * @param <DEVICE> the type of the devices
 * @param <DEVICES> the type of the devices list
 * @param <AUTOMATION> the type of the automations
 * @param <AUTOMATIONS> the type of the automations list
 * @param <COMMAND> the type of the command
 * @param <ROOT> the type of the root
 */
public abstract class ProxyRootObject<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            USER extends ProxyUser<?, ?, ?, USER>,
            USERS extends ProxyList<?, ?, ?, USER, USERS>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            TYPES extends ProxyList<?, ?, ?, TYPE, TYPES>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?, ?>,
            DEVICES extends ProxyList<?, ?, ?, DEVICE, DEVICES>,
            AUTOMATION extends ProxyAutomation<?, ?, ?, ?, ?, ?, ?, ?, ?>,
            AUTOMATIONS extends ProxyList<?, ?, ?, AUTOMATION, AUTOMATIONS>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            ROOT extends ProxyRootObject<RESOURCES, CHILD_RESOURCES, USER, USERS, TYPE, TYPES, DEVICE, DEVICES, AUTOMATION, AUTOMATIONS, COMMAND, ROOT>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, RootWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, ROOT, ProxyRootListener<? super ROOT>>
        implements ProxyRoot<USERS, TYPES, DEVICES, AUTOMATIONS, COMMAND, ROOT>, WrapperListener<HousemateObject<?, ?, ?, ?, ?>>, ConnectionStatusChangeListener {

    private USERS proxyConnectionList;
    private TYPES proxyTypeList;
    private DEVICES proxyDeviceList;
    private AUTOMATIONS proxyAutomationList;
    private COMMAND addUserCommand;
    private COMMAND addDeviceCommand;
    private COMMAND addAutomationCommand;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;
    
    private final static Set<String> toLoad = Sets.newHashSet();
    private ListenerRegistration loadRegistration;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     */
    public ProxyRootObject(RESOURCES resources, CHILD_RESOURCES childResources) {
        super(resources, childResources, new RootWrappable());
        routerRegistration = resources.getRouter().registerReceiver(this);
        connectionManager = new ConnectionManager(routerRegistration, ConnectionType.Proxy, ConnectionStatus.Unauthenticated);
        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return connectionManager.getStatus();
    }

    @Override
    public String getConnectionId() {
        return connectionManager.getConnectionId();
    }

    @Override
    public void login(AuthenticationMethod method) {
        connectionManager.login(method);
    }

    @Override
    public void logout() {
        connectionManager.logout();
        routerRegistration.remove();
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addWrapperListener(this));
        result.add(connectionManager.addStatusChangeListener(this));
        result.add(addMessageListener(CONNECTION_RESPONSE_TYPE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                connectionManager.authenticationResponseReceived(message.getPayload());
                if(!(message.getPayload() instanceof ReconnectResponse) && getStatus() == ConnectionStatus.Authenticated) {
                    loadRegistration = addWrapperListener(new WrapperListener<ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
                        @Override
                        public void childWrapperAdded(String childId, ProxyObject<?, ?, ?, ?, ?, ?, ?> wrapper) {
                            toLoad.remove(wrapper.getId());
                            if(toLoad.size() == 0) {
                                allLoaded();
                            }
                        }

                        @Override
                        public void childWrapperRemoved(String childId, ProxyObject<?, ?, ?, ?, ?, ?, ?> wrapper) {
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
                    });
                    toLoad.addAll(Lists.newArrayList(USERS_ID, TYPES_ID, DEVICES_ID, AUTOMATIONS_ID, ADD_USER_ID, ADD_DEVICE_ID, ADD_AUTOMATION_ID));
                    for(String name : toLoad)
                        load(name);
                }
            }
        }));
        result.add(addMessageListener(STATUS_TYPE, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.routerStatusChanged(message.getPayload());
            }
        }));
        return result;
    }

    /**
     * Notifies that all the objects required by the root have been loaded
     */
    private void allLoaded() {
        loadRegistration.removeListener();
        getChildObjects();
        for(ProxyRootListener<? super ROOT> listener : getObjectListeners())
            listener.loaded(getThis());
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
        proxyConnectionList = (USERS)getWrapper(USERS_ID);
        proxyTypeList = (TYPES)getWrapper(TYPES_ID);
        proxyDeviceList = (DEVICES)getWrapper(DEVICES_ID);
        proxyAutomationList = (AUTOMATIONS)getWrapper(AUTOMATIONS_ID);
        addUserCommand = (COMMAND)getWrapper(ADD_USER_ID);
        addDeviceCommand = (COMMAND)getWrapper(ADD_DEVICE_ID);
        addAutomationCommand = (COMMAND)getWrapper(ADD_AUTOMATION_ID);
    }

    @Override
    public USERS getUsers() {
        return proxyConnectionList;
    }

    @Override
    public TYPES getTypes() {
        return proxyTypeList;
    }

    @Override
    public DEVICES getDevices() {
        return proxyDeviceList;
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return proxyAutomationList;
    }

    @Override
    public COMMAND getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public COMMAND getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public COMMAND getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public void childWrapperAdded(String childName, HousemateObject<?, ?, ?, ?, ?> wrapper) {
        // do nothing
    }

    @Override
    public void childWrapperRemoved(String childName, HousemateObject<?, ?, ?, ?, ?> wrapper) {
        // do nothing
    }

    @Override
    public void ancestorAdded(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        if(wrapper instanceof HousemateObject)
            objectWrapperAdded(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) wrapper);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectWrapperAdded(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getWrappers())
            objectWrapperAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorRemoved(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        if(wrapper instanceof HousemateObject)
            objectWrapperRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) wrapper);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectWrapperRemoved(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getWrappers())
            objectWrapperRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = new Listeners<ObjectLifecycleListener>();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    @Override
    public final void connectionStatusChanged(ConnectionStatus status) {
        for(ProxyRootListener<? super ROOT> listener : getObjectListeners())
            listener.connectionStatusChanged(getThis(), status);
    }

    @Override
    public final void brokerInstanceChanged() {
        Set<String> ids = Sets.newHashSet();
        for(HousemateObject<?, ?, ?, ?, ?> child : getWrappers()) {
            child.uninit();
            ids.add(child.getId());
        }
        for(String id : ids)
            removeWrapper(id);
        for(ProxyRootListener<? super ROOT> listener : getObjectListeners())
            listener.brokerInstanceChanged(getThis());
    }
}
