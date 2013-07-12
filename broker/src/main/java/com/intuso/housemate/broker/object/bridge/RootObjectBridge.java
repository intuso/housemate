package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Joiner;
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
import com.intuso.housemate.object.broker.proxy.BrokerProxyDevice;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;
import com.intuso.housemate.object.broker.real.BrokerRealAutomation;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.object.Object;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class RootObjectBridge
        extends BridgeObject<RootData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
            RootObjectBridge, RootListener<? super RootObjectBridge>>
        implements Root<RootObjectBridge> {

    private ListBridge<UserData, BrokerRealUser, UserBridge> users;
    private ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types;
    private ListBridge<DeviceData, BrokerProxyDevice, DeviceBridge> devices;
    private ListBridge<AutomationData, BrokerRealAutomation, AutomationBridge> automations;
    private CommandBridge addUser;
    private CommandBridge addDevice;
    private CommandBridge addAutomation;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    public RootObjectBridge(final BrokerBridgeResources resources) {
        super(resources, new RootData());
        resources.setRoot(this);
        users = new ListBridge<UserData, BrokerRealUser, UserBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getUsers(),
                new UserBridge.Converter(resources));
        types = new ListBridge<TypeData<?>, BrokerProxyType, TypeBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getTypes(),
                new TypeBridge.Converter(resources));
        devices = new ListBridge<DeviceData, BrokerProxyDevice, DeviceBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getDevices(),
                new DeviceBridge.Converter(resources));
        automations = new ListBridge<AutomationData, BrokerRealAutomation, AutomationBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getAutomations(),
                new AutomationBridge.Converter(resources));
        addUser = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddUserCommand());
        addDevice = new CommandBridge(resources, resources.getGeneralResources().getClient().getAddDeviceCommand());
        addAutomation = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddAutomationCommand());
        addChild(users);
        addChild(types);
        addChild(devices);
        addChild(automations);
        addChild(addUser);
        addChild(addDevice);
        addChild(addAutomation);
        getResources().getGeneralResources().getStorage().watchDevices(devices);
        getResources().getGeneralResources().getStorage().watchAutomations(automations);
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

    public ListBridge<UserData, BrokerRealUser, UserBridge> getUsers() {
        return users;
    }

    public ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> getTypes() {
        return types;
    }

    public ListBridge<DeviceData, BrokerProxyDevice, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<AutomationData, BrokerRealAutomation, AutomationBridge> getAutomations() {
        return automations;
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, Object<?, ?, ?, ?> ancestor) {
        super.ancestorObjectAdded(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) ancestor);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, com.intuso.utilities.object.Object<?, ?, ?, ?> ancestor) {
        super.ancestorObjectRemoved(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) ancestor);
    }

    private void objectAdded(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(Object.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + Object.PATH_SEPARATOR + child.getId(), child);
    }

    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(Object.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + Object.PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(Object.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = new Listeners<ObjectLifecycleListener>();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
