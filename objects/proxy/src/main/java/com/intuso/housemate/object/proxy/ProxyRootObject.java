package com.intuso.housemate.object.proxy;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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
            USER extends ProxyUser<?, USER>,
            USERS extends ProxyList<?, USER, USERS>,
            TYPE extends ProxyType<?, ?, ?, ?>,
            TYPES extends ProxyList<?, TYPE, TYPES>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?>,
            DEVICES extends ProxyList<?, DEVICE, DEVICES>,
            AUTOMATION extends ProxyAutomation<?, ?, ?, ?, ?, ?, ?>,
            AUTOMATIONS extends ProxyList<?, AUTOMATION, AUTOMATIONS>,
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            ROOT extends ProxyRootObject<USER, USERS, TYPE, TYPES, DEVICE, DEVICES, AUTOMATION, AUTOMATIONS, COMMAND, ROOT>>
        extends ProxyObject<RootData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, ROOT, RootListener<? super ROOT>>
        implements ProxyRoot<USERS, TYPES, DEVICES, AUTOMATIONS, COMMAND, ROOT>, ObjectListener<ProxyObject<?, ?, ?, ?, ?>>, ConnectionStatusChangeListener {

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param router The router to connect through
     */
    public ProxyRootObject(Log log, Injector injector, Router router) {
        super(log, injector, new RootData());
        routerRegistration = router.registerReceiver(this);
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
        result.add(addChildListener(this));
        result.add(connectionManager.addStatusChangeListener(this));
        result.add(addMessageListener(CONNECTION_RESPONSE_TYPE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                connectionManager.authenticationResponseReceived(message.getPayload());
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

    @Override
    public USERS getUsers() {
        return (USERS) getChild(USERS_ID);
    }

    @Override
    public TYPES getTypes() {
        return (TYPES) getChild(TYPES_ID);
    }

    @Override
    public DEVICES getDevices() {
        return (DEVICES) getChild(DEVICES_ID);
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return (AUTOMATIONS) getChild(AUTOMATIONS_ID);
    }

    @Override
    public COMMAND getAddUserCommand() {
        return (COMMAND) getChild(ADD_USER_ID);
    }

    @Override
    public COMMAND getAddDeviceCommand() {
        return (COMMAND) getChild(ADD_DEVICE_ID);
    }

    @Override
    public COMMAND getAddAutomationCommand() {
        return (COMMAND) getChild(ADD_AUTOMATION_ID);
    }

    @Override
    public void childObjectAdded(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
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
        for(RootListener<? super ROOT> listener : getObjectListeners())
            listener.connectionStatusChanged(getThis(), status);
    }

    @Override
    public final void newServerInstance() {
        Set<String> ids = Sets.newHashSet();
        for(HousemateObject<?, ?, ?, ?> child : getChildren()) {
            child.uninit();
            ids.add(child.getId());
        }
        for(String id : ids)
            removeChild(id);
        for(RootListener<? super ROOT> listener : getObjectListeners())
            listener.newServerInstance(getThis());
    }
}
