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
import com.intuso.housemate.api.object.connection.ClientWrappable;
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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/08/12
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyRootObject<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            U extends ProxyUser<?, ?, ?, U>,
            PUL extends ProxyList<?, ?, ?, U, PUL>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            PTL extends ProxyList<?, ?, ?, T, PTL>,
            D extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?, ?>,
            PDL extends ProxyList<?, ?, ?, D, PDL>,
            Ru extends ProxyAutomation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
            PRL extends ProxyList<?, ?, ?, Ru, PRL>,
            C extends ProxyCommand<?, ?, ?, ?, C>,
            RO extends ProxyRootObject<R, SR, U, PUL, T, PTL, D, PDL, Ru, PRL, C, RO>>
        extends ProxyObject<R, SR, RootWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, RO, ProxyRootListener<? super RO>>
        implements ProxyRoot<PUL, PTL, PDL, PRL, C, RO>, WrapperListener<HousemateObject<?, ?, ?, ?, ?>>, ConnectionStatusChangeListener {

    private PUL proxyConnectionList;
    private PTL proxyTypeList;
    private PDL proxyDeviceList;
    private PRL proxyAutomationList;
    private C addUserCommand;
    private C addDeviceCommand;
    private C addAutomationCommand;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;
    
    private final static Set<String> toLoad = Sets.newHashSet();
    private ListenerRegistration loadRegistration;

    public ProxyRootObject(R resources, SR subResources) {
        super(resources, subResources, new RootWrappable());
        routerRegistration = resources.getRouter().registerReceiver(this);
        connectionManager = new ConnectionManager(routerRegistration, ClientWrappable.Type.Proxy, ConnectionStatus.Unauthenticated);
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
        result.add(addMessageListener(CONNECTION_RESPONSE, new Receiver<AuthenticationResponse>() {
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
                    toLoad.addAll(Lists.newArrayList(USERS, TYPES, DEVICES, AUTOMATIONS, ADD_USER, ADD_DEVICE, ADD_AUTOMATION));
                    for(String name : toLoad)
                        load(name);
                }
            }
        }));
        result.add(addMessageListener(STATUS, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.routerStatusChanged(message.getPayload());
            }
        }));
        return result;
    }

    private void allLoaded() {
        loadRegistration.removeListener();
        getChildObjects();
        for(ProxyRootListener<? super RO> listener : getObjectListeners())
            listener.loaded(getThis());
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
        proxyConnectionList = (PUL)getWrapper(USERS);
        proxyTypeList = (PTL)getWrapper(TYPES);
        proxyDeviceList = (PDL)getWrapper(DEVICES);
        proxyAutomationList = (PRL)getWrapper(AUTOMATIONS);
        addUserCommand = (C)getWrapper(ADD_USER);
        addDeviceCommand = (C)getWrapper(ADD_DEVICE);
        addAutomationCommand = (C)getWrapper(ADD_AUTOMATION);
    }

    @Override
    public PUL getUsers() {
        return proxyConnectionList;
    }

    @Override
    public PTL getTypes() {
        return proxyTypeList;
    }

    @Override
    public PDL getDevices() {
        return proxyDeviceList;
    }

    @Override
    public PRL getAutomations() {
        return proxyAutomationList;
    }

    @Override
    public C getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public C getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public C getAddAutomationCommand() {
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

    private void objectWrapperAdded(String path, HousemateObject<?, ?, ?, ?, ?> objectWrapper) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, objectWrapper);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : objectWrapper.getWrappers())
            objectWrapperAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorRemoved(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        if(wrapper instanceof HousemateObject)
            objectWrapperRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) wrapper);
    }

    private void objectWrapperRemoved(String path, HousemateObject<?, ?, ?, ?, ?> objectWrapper) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, objectWrapper);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : objectWrapper.getWrappers())
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
        for(ProxyRootListener<? super RO> listener : getObjectListeners())
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
        for(ProxyRootListener<? super RO> listener : getObjectListeners())
            listener.brokerInstanceChanged(getThis());
    }
}
