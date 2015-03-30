package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.HasApplications;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.HasAutomations;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HasHardwares;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.HasUsers;
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
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.List;

public class RealRoot
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RootListener<? super RealRoot>>
        implements Root<RealRoot>,
            HasApplications<RealList<ApplicationData, RealApplication>>,
            HasAutomations<RealList<AutomationData, RealAutomation>>,
            HasDevices<RealList<DeviceData, RealDevice>>,
            HasHardwares<RealList<HardwareData, RealHardware>>,
            HasTypes<RealList<TypeData<?>, RealType<?, ?, ?>>>,
            HasUsers<RealList<UserData, RealUser>>,
            RealHardwareOwner, RealDeviceOwner, RealAutomationOwner, RealUserOwner {

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
                    AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                    AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory) {
        super(log, listenersFactory, new RootData());

        properties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory, properties);

        this.applications = new RealList<>(log, listenersFactory, APPLICATIONS_ID, "Applications", "Applications");
        this.automations = new RealList<>(log, listenersFactory, AUTOMATIONS_ID, "Automations", "Automations");
        this.devices = new RealList<>(log, listenersFactory, DEVICES_ID, "Devices", "Devices");
        this.hardwares = new RealList<>(log, listenersFactory, HARDWARES_ID, "Hardware", "Hardware");
        this.types = new RealList<>(log, listenersFactory, TYPES_ID, "Types", "Types");
        this.users = new RealList<>(log, listenersFactory, USERS_ID, "Users", "Users");

        this.addAutomationCommand = addAutomationCommandFactory.create(this);
        this.addDeviceCommand = addDeviceCommandFactory.create(this);
        this.addHardwareCommand = addHardwareCommandFactory.create(this);
        this.addUserCommand = addUserCommandFactory.create(this);

        this.connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Real, this);

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
