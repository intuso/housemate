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
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.ObjectRoot;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.RealApplication;
import com.intuso.housemate.object.real.RealAutomation;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.RealUser;
import com.intuso.housemate.object.server.ServerProxyDevice;
import com.intuso.housemate.object.server.ServerProxyHardware;
import com.intuso.housemate.object.server.ServerProxyRoot;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.housemate.object.server.client.ClientPayload;
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

    private final ListBridge<ApplicationData, RealApplication, ApplicationBridge> applications;
    private final ListBridge<UserData, RealUser, UserBridge> users;
    private final MultiListBridge<HardwareData, ServerProxyHardware, HardwareBridge> hardwares;
    private final MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;
    private final MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge> devices;
    private final ListBridge<AutomationData, RealAutomation, AutomationBridge> automations;
    private final CommandBridge addUser;
    private final CommandBridge addHardware;
    private final CommandBridge addDevice;
    private final CommandBridge addAutomation;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    @Inject
    public RootBridge(Log log, ListenersFactory listenersFactory, RealRoot realRoot,
                      MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new RootData());
        this.listenersFactory = listenersFactory;
        applications = new SingleListBridge<ApplicationData, RealApplication, ApplicationBridge>(
                log, listenersFactory, realRoot.getApplications(), new ApplicationBridge.Converter(log, listenersFactory, types));
        users = new SingleListBridge<UserData, RealUser, UserBridge>(log, listenersFactory,
                realRoot.getUsers(), new UserBridge.Converter(log, listenersFactory, types));
        hardwares = new MultiListBridge<HardwareData, ServerProxyHardware, HardwareBridge>(log, listenersFactory,
                new ListData<HardwareData>(ObjectRoot.HARDWARES_ID, "Hardwares", "Connected hardware"),
                new HardwareBridge.Converter(log, listenersFactory, types));
        this.types = types;
        devices = new MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge>(log, listenersFactory,
                new ListData<DeviceData>(ObjectRoot.DEVICES_ID, "Devices", "Devices"),
                new DeviceBridge.Converter(log, listenersFactory, types));
        automations = new SingleListBridge<AutomationData, RealAutomation, AutomationBridge>(log, listenersFactory,
                realRoot.getAutomations(), new AutomationBridge.Converter(log, listenersFactory, types));
        addUser = new CommandBridge(log, listenersFactory, realRoot.getAddUserCommand(), types);
        addHardware = new CommandBridge(log, listenersFactory, realRoot.getAddHardwareCommand(), types);
        addDevice = new CommandBridge(log, listenersFactory, realRoot.getAddDeviceCommand(), types);
        addAutomation = new CommandBridge(log, listenersFactory, realRoot.getAddAutomationCommand(), types);
        addChild(applications);
        addChild(users);
        addChild(hardwares);
        addChild(types);
        addChild(devices);
        addChild(automations);
        addChild(addUser);
        addChild(addHardware);
        addChild(addDevice);
        addChild(addAutomation);
        init(null);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ObjectRoot.CLEAR_LOADED, new Receiver<ClientPayload<StringPayload>>() {
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

    public ListBridge<UserData, RealUser, UserBridge> getUsers() {
        return users;
    }

    public MultiListBridge<HardwareData, ServerProxyHardware, HardwareBridge> getHardwares() {
        return hardwares;
    }

    public MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> getTypes() {
        return types;
    }

    public MultiListBridge<DeviceData, ServerProxyDevice, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<AutomationData, RealAutomation, AutomationBridge> getAutomations() {
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
        hardwares.addList(root.getHardwares());
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
