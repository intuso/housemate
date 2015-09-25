package com.intuso.housemate.client.real.api.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.real.api.internal.factory.automation.RealAutomationOwner;
import com.intuso.housemate.client.real.api.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.api.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.device.RealDeviceOwner;
import com.intuso.housemate.client.real.api.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.api.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.hardware.RealHardwareOwner;
import com.intuso.housemate.client.real.api.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.user.AddUserCommand;
import com.intuso.housemate.client.real.api.internal.factory.user.RealUserOwner;
import com.intuso.housemate.comms.api.internal.*;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;
import java.util.Map;

public class RealRoot
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, ClientRoot.Listener<? super RealRoot>>
        implements ClientRoot<ClientRoot.Listener<? super RealRoot>, RealRoot>,
        ObjectRoot<ClientRoot.Listener<? super RealRoot>, RealRoot>,
        Application.Container<RealList<ApplicationData, RealApplication>>,
        Automation.Container<RealList<AutomationData, RealAutomation>>,
        Device.Container<RealList<DeviceData, RealDevice>>,
        Hardware.Container<RealList<HardwareData, RealHardware>>,
        Type.Container<RealList<TypeData<?>, RealType<?, ?, ?>>>,
        User.Container<RealList<UserData, RealUser>>,
        RealHardwareOwner, RealDeviceOwner, RealAutomationOwner, RealUserOwner {

    public final static String SEND_INITIAL_DATA = "send-initial-data";
    public final static String INITIAL_DATA = "initial-data";

    public final static String APPLICATIONS_ID = "applications";
    public final static String USERS_ID = "users";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_USER_ID = "add-user";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_AUTOMATION_ID = "add-automation";

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    private final RealList<ApplicationData, RealApplication> applications;
    private final RealList<AutomationData, RealAutomation> automations;
    private final RealList<DeviceData, RealDevice> devices;
    private final RealList<HardwareData, RealHardware> hardwares;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealList<UserData, RealUser> users;

    private final RealCommand addAutomationCommand;
    private final RealCommand addDeviceCommand;
    private final RealCommand addHardwareCommand;
    private final RealCommand addUserCommand;

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    @Inject
    public RealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                    RealList<TypeData<?>, RealType<?, ?, ?>> types,
                    AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                    AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory,
                    ConditionFactoryType conditionFactoryType, DeviceFactoryType deviceFactoryType,
                    HardwareFactoryType hardwareFactoryType, TaskFactoryType taskFactoryType) {
        super(log, listenersFactory, new RootData());

        this.applications = new RealList<>(log, listenersFactory, APPLICATIONS_ID, "Applications", "Applications");
        this.automations = new RealList<>(log, listenersFactory, AUTOMATIONS_ID, "Automations", "Automations");
        this.devices = new RealList<>(log, listenersFactory, DEVICES_ID, "Devices", "Devices");
        this.hardwares = new RealList<>(log, listenersFactory, HARDWARES_ID, "Hardware", "Hardware");
        this.types = types;
        this.users = new RealList<>(log, listenersFactory, USERS_ID, "Users", "Users");

        this.addAutomationCommand = addAutomationCommandFactory.create(this);
        this.addDeviceCommand = addDeviceCommandFactory.create(this);
        this.addHardwareCommand = addHardwareCommandFactory.create(this);
        this.addUserCommand = addUserCommandFactory.create(this);

        this.connectionManager = new ConnectionManager(listenersFactory, properties, ApplicationRegistration.ClientType.Real, this);

        // need to do this once the connection manager is created and once the object is init'ed so the path is not null
        this.routerRegistration = router.registerReceiver(this);

        addChild(applications);
        addChild(automations);
        addChild(devices);
        addChild(hardwares);
        addChild(types);
        addChild(users);

        addChild(addAutomationCommand);
        addChild(addDeviceCommand);
        addChild(addHardwareCommand);
        addChild(addUserCommand);

        addType(conditionFactoryType);
        addType(deviceFactoryType);
        addType(hardwareFactoryType);
        addType(taskFactoryType);

        init(null);
    }

    @Override
    public Application.Status getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        connectionManager.register(applicationDetails, component);
    }

    @Override
    public void unregister() {
        connectionManager.unregister();
    }

    @Override
    public void sendMessage(Message<?> message) {
        // if we're not allowed to send messages, and it's not a registration message, then throw an exception
        if(checkCanSendMessage(message))
            routerRegistration.sendMessage(message);
    }

    public boolean checkCanSendMessage(Message<?> message) {
        if(getApplicationInstanceStatus() != ApplicationInstance.Status.Allowed
                && !(message.getPath().length == 1 &&
                (message.getType().equals(ApplicationRegistration.APPLICATION_REGISTRATION_TYPE)
                        || message.getType().equals(ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE))))
            throw new HousemateCommsException("Client application instance is not allowed access to the server");
        return true;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for(ClientRoot.Listener<? super RealRoot> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(RealRoot.this, serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(Application.Status applicationStatus) {
                for(ClientRoot.Listener<? super RealRoot> listener : getObjectListeners())
                    listener.applicationStatusChanged(RealRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstance.Status applicationInstanceStatus) {
                for(ClientRoot.Listener<? super RealRoot> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(RealRoot.this, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for (ClientRoot.Listener<? super RealRoot> listener : getObjectListeners())
                    listener.newApplicationInstance(RealRoot.this, instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                for (ClientRoot.Listener<? super RealRoot> listener : getObjectListeners())
                    listener.newServerInstance(RealRoot.this, serverId);
            }
        }));
        result.add(addMessageListener(RootData.SERVER_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.SERVER_CONNECTION_STATUS_TYPE, new Message.Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                connectionManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                connectionManager.setApplicationInstanceStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(SEND_INITIAL_DATA, new Message.Receiver<NoPayload>() {
            @Override
            public void messageReceived(Message<NoPayload> message) {
                sendMessage(INITIAL_DATA, getData());
            }
        }));
        return result;
    }

    @Override
    public final RealList<TypeData<?>, RealType<?, ?, ?>> getTypes() {
        return types;
    }

    public final void addType(RealType<?, ?, ?> type) {
        types.add(type);
    }

    public final void removeType(String name) {
        types.remove(name);
    }

    @Override
    public RealList<HardwareData, RealHardware> getHardwares() {
        return hardwares;
    }

    @Override
    public ChildOverview getAddHardwareCommandDetails() {
        return new ChildOverview(ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware");
    }

    public RealCommand getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public final void addHardware(RealHardware Hardware) {
        hardwares.add(Hardware);
    }

    @Override
    public final void removeHardware(RealHardware realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public final RealList<DeviceData, RealDevice> getDevices() {
        return devices;
    }

    @Override
    public ChildOverview getAddDeviceCommandDetails() {
        return new ChildOverview(ADD_DEVICE_ID, ADD_DEVICE_ID, "Add a device");
    }

    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add(device);
    }

    @Override
    public final void removeDevice(RealDevice realDevice) {
        devices.remove(realDevice.getId());
    }

    @Override
    public RealList<AutomationData, RealAutomation> getAutomations() {
        return automations;
    }

    @Override
    public ChildOverview getAddAutomationCommandDetails() {
        return new ChildOverview(ADD_AUTOMATION_ID, ADD_AUTOMATION_ID, "Add an automation");
    }

    public RealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public final void addAutomation(RealAutomation automation) {
        automations.add(automation);
    }

    @Override
    public final void removeAutomation(RealAutomation realAutomation) {
        automations.remove(realAutomation.getId());
    }

    @Override
    public RealList<ApplicationData, RealApplication> getApplications() {
        return applications;
    }

    @Override
    public RealList<UserData, RealUser> getUsers() {
        return users;
    }

    @Override
    public ChildOverview getAddUserCommandDetails() {
        return new ChildOverview(ADD_USER_ID, ADD_USER_ID, "Add a user");
    }

    public RealCommand getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public final void addUser(RealUser user) {
        users.add(user);
    }

    @Override
    public final void removeUser(RealUser realUser) {
        users.remove(realUser.getId());
    }

    @Override
    public void childObjectAdded(String childName, RealObject<?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, RealObject<?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectAdded(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectRemoved(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = getListenersFactory().create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
