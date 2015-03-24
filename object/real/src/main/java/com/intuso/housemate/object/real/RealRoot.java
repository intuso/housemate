package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.root.ObjectRoot;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.factory.automation.AddAutomationCommand;
import com.intuso.housemate.object.real.factory.automation.RealAutomationOwner;
import com.intuso.housemate.object.real.factory.device.AddDeviceCommand;
import com.intuso.housemate.object.real.factory.device.RealDeviceOwner;
import com.intuso.housemate.object.real.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.object.real.factory.hardware.RealHardwareOwner;
import com.intuso.housemate.object.real.factory.user.AddUserCommand;
import com.intuso.housemate.object.real.factory.user.RealUserOwner;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

public class RealRoot
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RootListener<? super RealRoot>>
        implements ObjectRoot<
                            RealList<TypeData<?>, RealType<?, ?, ?>>,
                            RealList<HardwareData, RealHardware>,
                            RealList<DeviceData, RealDevice>,
                            RealList<AutomationData, RealAutomation>,
                            RealList<ApplicationData, RealApplication>,
                            RealList<UserData, RealUser>,
                            RealCommand,
                            RealRoot>,
            RealHardwareOwner, RealDeviceOwner, RealAutomationOwner, RealUserOwner {

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealList<HardwareData, RealHardware> hardwares;
    private final RealCommand addHardwareCommand;
    private final RealList<DeviceData, RealDevice> devices;
    private final RealCommand addDeviceCommand;
    private final RealList<AutomationData, RealAutomation> automations;
    private final RealCommand addAutomationCommand;
    private final RealList<ApplicationData, RealApplication> applications;
    private final RealList<UserData, RealUser> users;
    private final RealCommand addUserCommand;

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    private boolean resend = false;

    @Inject
    public RealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                    RealList<TypeData<?>, RealType<?, ?, ?>> types,
                    AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                    AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory) {
        super(log, listenersFactory, new RootData());

        this.types = types;
        this.hardwares = new RealList<>(log, listenersFactory, HARDWARES_ID, HARDWARES_ID, "Connected hardware");
        this.addHardwareCommand = addHardwareCommandFactory.create(this);
        this.devices = new RealList<>(log, listenersFactory, DEVICES_ID, DEVICES_ID, "Defined devices");
        this.addDeviceCommand = addDeviceCommandFactory.create(this);
        this.automations = new RealList<>(log, listenersFactory, AUTOMATIONS_ID, AUTOMATIONS_ID, "Defined automations");
        this.addAutomationCommand = addAutomationCommandFactory.create(this);
        this.applications = new RealList<>(log, listenersFactory, APPLICATIONS_ID, APPLICATIONS_ID, "Connected applications");
        this.users = new RealList<>(log, listenersFactory, USERS_ID, USERS_ID, "Defined users");
        this.addUserCommand = addUserCommandFactory.create(this);

        this.connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Real, this);

        // need to do this once the connection manager is created and once the object is init'ed so the path is not null
        this.routerRegistration = router.registerReceiver(this);

        addChild(types);
        addChild(hardwares);
        addChild(addHardwareCommand);
        addChild(devices);
        addChild(addDeviceCommand);
        addChild(automations);
        addChild(addAutomationCommand);
        addChild(applications);
        addChild(users);
        addChild(addUserCommand);

        init(null);
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        connectionManager.register(applicationDetails);
    }

    @Override
    public void unregister() {
        connectionManager.unregister();
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void sendMessage(Message<?> message) {
        // if we're not allowed to send messages, and it's not a registration message, then throw an exception
        if(checkCanSendMessage(message))
            routerRegistration.sendMessage(message);
    }

    public boolean checkCanSendMessage(Message<?> message) {
        if(getApplicationInstanceStatus() != ApplicationInstanceStatus.Allowed
                && !(message.getPath().length == 1 &&
                (message.getType().equals(APPLICATION_REGISTRATION_TYPE)
                        || message.getType().equals(APPLICATION_UNREGISTRATION_TYPE))))
            throw new HousemateRuntimeException("Client application instance is not allowed access to the server");
        return true;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for(RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(RealRoot.this, serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(ApplicationStatus applicationStatus) {
                for(RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.applicationStatusChanged(RealRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstanceStatus applicationInstanceStatus) {
                for(RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(RealRoot.this, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for (RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.newApplicationInstance(RealRoot.this, instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                resend = true; // set to true. when access allowed above method will resend objects
                for (RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.newServerInstance(RealRoot.this, serverId);
            }
        }));
        result.add(addMessageListener(SERVER_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(SERVER_CONNECTION_STATUS_TYPE, new Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) throws HousemateException {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_STATUS_TYPE, new Receiver<ApplicationStatus>() {
            @Override
            public void messageReceived(Message<ApplicationStatus> message) throws HousemateException {
                connectionManager.setApplicationStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_STATUS_TYPE, new Receiver<ApplicationInstanceStatus>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceStatus> message) throws HousemateException {
                connectionManager.setApplicationInstanceStatus(message.getPayload());
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new hardware";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "Description for the new hardware";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new hardware";
}
