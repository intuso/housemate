package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.realclient.RealClient;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.realclient.RealClientListener;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.server.comms.ClientInstance;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealClientBridge
        extends BridgeObject<RealClientData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
            RealClientBridge, RealClientListener<? super RealClientBridge>>
        implements RealClient<
        ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>,
        ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>,
        ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>,
        ConvertingListBridge<HardwareData, Hardware<?, ?>, HardwareBridge>,
        ConvertingListBridge<TypeData<?>, Type, TypeBridge>,
        ConvertingListBridge<UserData, User<?, ?>, UserBridge>,
            CommandBridge,
            RealClientBridge> {

    private final ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge> applications;
    private final ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> automations;
    private final ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> devices;
    private final ConvertingListBridge<HardwareData, Hardware<?, ?>, HardwareBridge> hardwares;
    private final ConvertingListBridge<TypeData<?>, Type, TypeBridge> types;
    private final ConvertingListBridge<UserData, User<?, ?>, UserBridge> users;
    private final CommandBridge addAutomation;
    private final CommandBridge addDevice;
    private final CommandBridge addHardware;
    private final CommandBridge addUser;

    public RealClientBridge(Log log, ListenersFactory listenersFactory,
                            ServerProxyRoot realClient) {
        super(log, listenersFactory, makeData(realClient.getClient().getClientInstance()));
        applications = new ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>(
                log, listenersFactory, realClient.getApplications(),
                new ApplicationBridge.Converter(log, listenersFactory));
        automations = new ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>(
                log, listenersFactory, realClient.getAutomations(),
                new AutomationBridge.Converter(log, listenersFactory));
        devices = new ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>(
                log, listenersFactory, realClient.getDevices(),
                new DeviceBridge.Converter(log, listenersFactory));
        hardwares = new ConvertingListBridge<HardwareData, Hardware<?, ?>, HardwareBridge>(
                log, listenersFactory, realClient.getHardwares(),
                new HardwareBridge.Converter(log, listenersFactory));
        types = new ConvertingListBridge<TypeData<?>, Type, TypeBridge>(
                log, listenersFactory, realClient.getTypes(),
                new TypeBridge.Converter(log, listenersFactory));
        users = new ConvertingListBridge<UserData, User<?, ?>, UserBridge>(
                log, listenersFactory, realClient.getUsers(),
                new UserBridge.Converter(log, listenersFactory));

        addAutomation = new CommandBridge(log, listenersFactory, realClient.getAddAutomationCommand());
        addDevice = new CommandBridge(log, listenersFactory, realClient.getAddDeviceCommand());
        addHardware = new CommandBridge(log, listenersFactory, realClient.getAddHardwareCommand());
        addUser = new CommandBridge(log, listenersFactory, realClient.getAddUserCommand());

        addChild(applications);
        addChild(automations);
        addChild(devices);
        addChild(hardwares);
        addChild(types);
        addChild(users);
        addChild(addAutomation);
        addChild(addDevice);
        addChild(addHardware);
        addChild(addUser);
    }

    private static RealClientData makeData(ClientInstance instance) {
        String id = instance.getApplicationDetails().getApplicationId() + "-" + instance.getApplicationInstanceId();
        String name = instance.getApplicationDetails().getApplicationName() + " - " + instance.getApplicationInstanceId();
        return new RealClientData(id, name, name);
    }

    @Override
    public ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge> getApplications() {
        return applications;
    }

    @Override
    public ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> getAutomations() {
        return automations;
    }

    @Override
    public ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> getDevices() {
        return devices;
    }

    @Override
    public ConvertingListBridge<HardwareData, Hardware<?, ?>, HardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public ConvertingListBridge<TypeData<?>, Type, TypeBridge> getTypes() {
        return types;
    }

    @Override
    public ConvertingListBridge<UserData, User<?, ?>, UserBridge> getUsers() {
        return users;
    }

    @Override
    public CommandBridge getAddAutomationCommand() {
        return addAutomation;
    }

    @Override
    public CommandBridge getAddDeviceCommand() {
        return addDevice;
    }

    @Override
    public CommandBridge getAddHardwareCommand() {
        return addHardware;
    }

    @Override
    public CommandBridge getAddUserCommand() {
        return addUser;
    }
}
