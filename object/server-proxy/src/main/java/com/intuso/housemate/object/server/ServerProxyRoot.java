package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.HasApplications;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.HasAutomations;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HasHardwares;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.HasUsers;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyRoot
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyRoot, RootListener<? super ServerProxyRoot>>
        implements Root<ServerProxyRoot>,
                HasTypes<ServerProxyList<TypeData<?>, ServerProxyType>>,
                HasHardwares<ServerProxyList<HardwareData, ServerProxyHardware>>,
                HasDevices<ServerProxyList<DeviceData, ServerProxyDevice>>,
                HasAutomations<ServerProxyList<AutomationData, ServerProxyAutomation>>,
                HasApplications<ServerProxyList<ApplicationData, ServerProxyApplication>>,
                HasUsers<ServerProxyList<UserData, ServerProxyUser>> {

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

    private ServerProxyList<TypeData<?>, ServerProxyType> types;
    private ServerProxyCommand addHardware;
    private ServerProxyList<HardwareData, ServerProxyHardware> hardwares;
    private ServerProxyCommand addDevice;
    private ServerProxyList<DeviceData, ServerProxyDevice> devices;
    private ServerProxyCommand addAutomation;
    private ServerProxyList<AutomationData, ServerProxyAutomation> automations;
    private ServerProxyList<ApplicationData, ServerProxyApplication> applications;
    private ServerProxyCommand addUser;
    private ServerProxyList<UserData, ServerProxyUser> users;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     */
    @Inject
    public ServerProxyRoot(Log log, ListenersFactory listenersFactory, Injector injector, ServerProxyList<TypeData<?>, ServerProxyType> types) {
        super(log, listenersFactory, injector, new RootData());

        this.types = types;
        hardwares = new ServerProxyList<>(log, listenersFactory, injector, new ListData<HardwareData>(HARDWARES_ID, HARDWARES_ID, "Proxied hardware"));
        addHardware = new ServerProxyCommand(log, listenersFactory, injector, new CommandData(ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware"));
        devices = new ServerProxyList<>(log, listenersFactory, injector, new ListData<DeviceData>(DEVICES_ID, DEVICES_ID, "Proxied devices"));
        addDevice = new ServerProxyCommand(log, listenersFactory, injector, new CommandData(ADD_DEVICE_ID, ADD_DEVICE_ID, "Add device"));
        automations = new ServerProxyList<>(log, listenersFactory, injector, new ListData<AutomationData>(AUTOMATIONS_ID, AUTOMATIONS_ID, "Proxied automations"));
        addAutomation = new ServerProxyCommand(log, listenersFactory, injector, new CommandData(ADD_AUTOMATION_ID, ADD_AUTOMATION_ID, "Add automation"));
        applications = new ServerProxyList<>(log, listenersFactory, injector, new ListData<ApplicationData>(APPLICATIONS_ID, APPLICATIONS_ID, "Proxied applications"));
        users = new ServerProxyList<>(log, listenersFactory, injector, new ListData<UserData>(USERS_ID, USERS_ID, "Proxied users"));
        addUser = new ServerProxyCommand(log, listenersFactory, injector, new CommandData(ADD_USER_ID, ADD_USER_ID, "Add user"));

        addChild(types);
        addChild(hardwares);
        addChild(addHardware);
        addChild(devices);
        addChild(addDevice);
        addChild(automations);
        addChild(addAutomation);
        addChild(applications);
        addChild(users);
        addChild(addUser);

        init(null);
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
        throw new HousemateRuntimeException("What ARE you trying to do!?!?");
    }

    public ServerProxyCommand getAddHardwareCommand() {
        return addHardware;
    }

    public ServerProxyCommand getAddDeviceCommand() {
        return addDevice;
    }

    public ServerProxyCommand getAddAutomationCommand() {
        return addAutomation;
    }

    public ServerProxyCommand getAddUserCommand() {
        return addUser;
    }

    @Override
    public ServerProxyList<ApplicationData, ServerProxyApplication> getApplications() {
        return applications;
    }

    @Override
    public ServerProxyList<AutomationData, ServerProxyAutomation> getAutomations() {
        return automations;
    }

    @Override
    public ServerProxyList<DeviceData, ServerProxyDevice> getDevices() {
        return devices;
    }

    @Override
    public ServerProxyList<HardwareData, ServerProxyHardware> getHardwares() {
        return hardwares;
    }

    @Override
    public ServerProxyList<TypeData<?>, ServerProxyType> getTypes() {
        return types;
    }

    @Override
    public ServerProxyList<UserData, ServerProxyUser> getUsers() {
        return users;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}
