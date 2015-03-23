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
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.ObjectRoot;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyRoot
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyRoot, RootListener<? super ServerProxyRoot>>
        implements ObjectRoot<
            ServerProxyList<TypeData<?>, ServerProxyType>,
            ServerProxyList<HardwareData, ServerProxyHardware>,
            ServerProxyList<DeviceData, ServerProxyDevice>,
            ServerProxyList<AutomationData, ServerProxyAutomation>,
            ServerProxyList<ApplicationData, ServerProxyApplication>,
            ServerProxyList<UserData, ServerProxyUser>,
            ServerProxyCommand,
            ServerProxyRoot> {

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
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("What ARE you trying to do!?!?");
    }

    @Override
    public ServerProxyCommand getAddHardwareCommand() {
        return addHardware;
    }

    @Override
    public ServerProxyCommand getAddDeviceCommand() {
        return addDevice;
    }

    @Override
    public ServerProxyCommand getAddAutomationCommand() {
        return addAutomation;
    }

    @Override
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
