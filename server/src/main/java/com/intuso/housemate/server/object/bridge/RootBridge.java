package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.proxy.ServerProxyDevice;
import com.intuso.housemate.object.server.proxy.ServerProxyRoot;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealApplication;
import com.intuso.housemate.object.server.real.ServerRealAutomation;
import com.intuso.housemate.object.server.real.ServerRealRoot;
import com.intuso.housemate.object.server.real.ServerRealUser;
import com.intuso.housemate.server.client.LocalClient;
import com.intuso.housemate.server.storage.persist.ServerObjectPersister;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootBridge
        extends BridgeObject<RootData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
        RootBridge, RootListener<? super RootBridge>>
        implements Root<RootBridge> {

    private final ListenersFactory listenersFactory;

    private final ListBridge<ApplicationData, ServerRealApplication, ApplicationBridge> applications;
    private final ListBridge<UserData, ServerRealUser, UserBridge> users;
    private final MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;
    private final MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge> devices;
    private final ListBridge<AutomationData, ServerRealAutomation, AutomationBridge> automations;
    private final CommandBridge addUser;
    private final CommandBridge addDevice;
    private final CommandBridge addAutomation;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    @Inject
    public RootBridge(Log log, ListenersFactory listenersFactory, ServerRealRoot realRoot,
                      MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> types,
                      LocalClient client, ServerObjectPersister storage) {
        super(log, listenersFactory, new RootData());
        this.listenersFactory = listenersFactory;
        applications = new SingleListBridge<ApplicationData, ServerRealApplication, ApplicationBridge>(
                log, listenersFactory, realRoot.getApplications(), new ApplicationBridge.Converter(log, listenersFactory, types));
        users = new SingleListBridge<UserData, ServerRealUser, UserBridge>(log, listenersFactory,
                realRoot.getUsers(), new UserBridge.Converter(log, listenersFactory, types));
        this.types = types;
        devices = new MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge>(log, listenersFactory,
                new ListData<DeviceData>(Root.DEVICES_ID, "Devices", "Devices"),
                new DeviceBridge.Converter(log, listenersFactory, types));
        automations = new SingleListBridge<AutomationData, ServerRealAutomation, AutomationBridge>(log, listenersFactory,
                realRoot.getAutomations(), new AutomationBridge.Converter(log, listenersFactory, types));
        addUser = new CommandBridge(log, listenersFactory, realRoot.getAddUserCommand(), types);
        addDevice = new CommandBridge(log, listenersFactory, client.getAddDeviceCommand(), types);
        addAutomation = new CommandBridge(log, listenersFactory, realRoot.getAddAutomationCommand(), types);
        addChild(applications);
        addChild(users);
        addChild(types);
        addChild(devices);
        addChild(automations);
        addChild(addUser);
        addChild(addDevice);
        addChild(addAutomation);
        storage.watchApplications(applications);
        storage.watchDevices(devices);
        storage.watchAutomations(automations);
        storage.watchUsers(users);
        init(null);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ProxyRoot.CLEAR_LOADED, new Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) throws HousemateException {
                clearClientInfo(message.getPayload().getClient());
            }
        }));
        return result;
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return ApplicationStatus.AllowInstances;
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return ApplicationInstanceStatus.Allowed;
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void unregister() {
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

    public MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> getTypes() {
        return types;
    }

    public MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<AutomationData, ServerRealAutomation, AutomationBridge> getAutomations() {
        return automations;
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectAdded(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectRemoved(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    public void addProxyRoot(ServerProxyRoot root) {
        devices.addList(root.getDevices());
        types.addList(root.getTypes());
    }

    private void objectAdded(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(BaseObject.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = listenersFactory.create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
