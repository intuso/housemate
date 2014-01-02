package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.server.proxy.ServerProxyDevice;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.server.client.LocalClient;
import com.intuso.housemate.server.storage.ServerObjectPersister;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealAutomation;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.object.server.real.ServerRealUser;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.object.BaseObject;

import java.util.HashMap;
import java.util.Map;

/**
 */
@Singleton
public class RootObjectBridge
        extends BridgeObject<RootData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
            RootObjectBridge, RootListener<? super RootObjectBridge>>
        implements Root<RootObjectBridge> {

    private ListBridge<UserData, ServerRealUser, UserBridge> users;
    private ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;
    private ListBridge<DeviceData, ServerProxyDevice, DeviceBridge> devices;
    private ListBridge<AutomationData, ServerRealAutomation, AutomationBridge> automations;
    private CommandBridge addUser;
    private CommandBridge addDevice;
    private CommandBridge addAutomation;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    @Inject
    public RootObjectBridge(final ServerBridgeResources resources, ServerRealRootObject realRoot,
                            ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types,
                            ServerProxyRootObject proxyRoot, LocalClient client, ServerObjectPersister storage) {
        super(resources, new RootData());
        resources.setRoot(this);
        users = new ListBridge<UserData, ServerRealUser, UserBridge>(resources,
                realRoot.getUsers(), new UserBridge.Converter(resources, types));
        this.types = types;
        devices = new ListBridge<DeviceData, ServerProxyDevice, DeviceBridge>(resources,
                proxyRoot.getDevices(), new DeviceBridge.Converter(resources, types));
        automations = new ListBridge<AutomationData, ServerRealAutomation, AutomationBridge>(resources,
                realRoot.getAutomations(), new AutomationBridge.Converter(resources, types));
        addUser = new CommandBridge(resources, realRoot.getAddUserCommand(), types);
        addDevice = new CommandBridge(resources, client.getAddDeviceCommand(), types);
        addAutomation = new CommandBridge(resources, realRoot.getAddAutomationCommand(), types);
        addChild(users);
        addChild(types);
        addChild(devices);
        addChild(automations);
        addChild(addUser);
        addChild(addDevice);
        addChild(addAutomation);
        storage.watchDevices(devices);
        storage.watchAutomations(automations);
        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return ConnectionStatus.Authenticated;
    }

    @Override
    public String getConnectionId() {
        return null;
    }

    @Override
    public void login(AuthenticationMethod method) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void logout() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatever");
    }

    public ListBridge<UserData, ServerRealUser, UserBridge> getUsers() {
        return users;
    }

    public ListBridge<TypeData<?>, ServerProxyType, TypeBridge> getTypes() {
        return types;
    }

    public ListBridge<DeviceData, ServerProxyDevice, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<AutomationData, ServerRealAutomation, AutomationBridge> getAutomations() {
        return automations;
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectAdded(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) ancestor);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectRemoved(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) ancestor);
    }

    private void objectAdded(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(BaseObject.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = new Listeners<ObjectLifecycleListener>();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
