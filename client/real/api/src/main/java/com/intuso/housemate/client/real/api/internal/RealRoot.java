package com.intuso.housemate.client.real.api.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.real.api.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.api.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.api.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.user.AddUserCommand;
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
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RealRoot.Listener>
        implements Root<RealRoot.Listener, RealRoot>,
        RequiresAccess,
        Message.Sender,
        ObjectRoot<RealRoot.Listener, RealRoot>,
        Application.Container<RealList<ApplicationData, RealApplication>>,
        Automation.Container<RealList<AutomationData, RealAutomation>>,
        Device.Container<RealList<DeviceData, RealDevice<?>>>,
        Hardware.Container<RealList<HardwareData, RealHardware<?>>>,
        Type.Container<RealList<TypeData<?>, RealType<?, ?, ?>>>,
        User.Container<RealList<UserData, RealUser>>,
        RealHardware.RemovedListener,
        AddHardwareCommand.Callback,
        RealDevice.RemovedListener,
        AddDeviceCommand.Callback,
        RealAutomation.RemovedListener,
        AddAutomationCommand.Callback,
        RealUser.RemovedListener,
        AddUserCommand.Callback {

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
    private final RealList<DeviceData, RealDevice<?>> devices;
    private final RealList<HardwareData, RealHardware<?>> hardwares;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealList<UserData, RealUser> users;

    private final RealCommand addAutomationCommand;
    private final RealCommand addDeviceCommand;
    private final RealCommand addHardwareCommand;
    private final RealCommand addUserCommand;

    private final Router.Registration routerRegistration;
    private final AccessManager accessManager;

    @Inject
    public RealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router<?> router,
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

        this.addAutomationCommand = addAutomationCommandFactory.create(ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware", this, this);
        this.addDeviceCommand = addDeviceCommandFactory.create(ADD_DEVICE_ID, ADD_DEVICE_ID, "Add a device", this, this);
        this.addHardwareCommand = addHardwareCommandFactory.create(ADD_AUTOMATION_ID, ADD_AUTOMATION_ID, "Add an automation", this, this);
        this.addUserCommand = addUserCommandFactory.create(ADD_USER_ID, ADD_USER_ID, "Add a user", this, this);

        this.accessManager = new AccessManager(listenersFactory, properties, ApplicationRegistration.ClientType.Real, this);

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

        // need to do this once the connection manager is created and once the object is init'ed so the path is not null
        this.routerRegistration = router.registerReceiver(new Router.Receiver() {
            @Override
            public void messageReceived(Message message) {
                distributeMessage(message);
            }

            @Override
            public void serverConnectionStatusChanged(ClientConnection clientConnection, ServerConnectionStatus serverConnectionStatus) {

            }

            @Override
            public void newServerInstance(ClientConnection clientConnection, String serverId) {

            }
        });
    }

    @Override
    public Application.Status getApplicationStatus() {
        return accessManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return accessManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        accessManager.register(applicationDetails, component);
    }

    @Override
    public void unregister() {
        accessManager.unregister();
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
    protected List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(accessManager.addStatusChangeListener(new RequiresAccess.Listener<AccessManager>() {

            @Override
            public void applicationStatusChanged(AccessManager accessManager, Application.Status applicationStatus) {
                for (Listener listener : getObjectListeners())
                    listener.applicationStatusChanged(RealRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(AccessManager accessManager, ApplicationInstance.Status applicationInstanceStatus) {
                for (Listener listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(RealRoot.this, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(AccessManager accessManager, String instanceId) {
                for (Listener listener : getObjectListeners())
                    listener.newApplicationInstance(RealRoot.this, instanceId);
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                accessManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                accessManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                accessManager.setApplicationInstanceStatus(message.getPayload().getStatus());
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
    public RealList<HardwareData, RealHardware<?>> getHardwares() {
        return hardwares;
    }

    public RealCommand getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public final void addHardware(RealHardware Hardware) {
        hardwares.add(Hardware);
    }

    @Override
    public final void hardwareRemoved(RealHardware realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public final RealList<DeviceData, RealDevice<?>> getDevices() {
        return devices;
    }

    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add(device);
    }

    @Override
    public final void deviceRemoved(RealDevice realDevice) {
        devices.remove(realDevice.getId());
    }

    @Override
    public RealList<AutomationData, RealAutomation> getAutomations() {
        return automations;
    }

    public RealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public final void addAutomation(RealAutomation automation) {
        automations.add(automation);
    }

    @Override
    public final void automationRemoved(RealAutomation realAutomation) {
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

    public RealCommand getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public final void addUser(RealUser user) {
        users.add(user);
    }

    @Override
    public final void userRemoved(RealUser realUser) {
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

    public interface Listener extends Root.Listener<RealRoot>, RequiresAccess.Listener<RealRoot> {}
}
