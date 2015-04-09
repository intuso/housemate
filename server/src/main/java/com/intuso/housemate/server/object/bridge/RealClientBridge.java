package com.intuso.housemate.server.object.bridge;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.Renameable;
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
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.persistence.api.Persistence;
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

    private final CommandBridge renameCommand;

    public RealClientBridge(Log log, ListenersFactory listenersFactory, ServerProxyRoot proxyRoot, final Persistence persistence) {
        super(log, listenersFactory, makeData(log, proxyRoot.getClient().getClientInstance(), persistence));
        applications = new ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>(
                log, listenersFactory, proxyRoot.getApplications(),
                new ApplicationBridge.Converter(log, listenersFactory));
        automations = new ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>(
                log, listenersFactory, proxyRoot.getAutomations(),
                new AutomationBridge.Converter(log, listenersFactory));
        devices = new ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>(
                log, listenersFactory, proxyRoot.getDevices(),
                new DeviceBridge.Converter(log, listenersFactory));
        hardwares = new ConvertingListBridge<HardwareData, Hardware<?, ?>, HardwareBridge>(
                log, listenersFactory, proxyRoot.getHardwares(),
                new HardwareBridge.Converter(log, listenersFactory));
        types = new ConvertingListBridge<TypeData<?>, Type, TypeBridge>(
                log, listenersFactory, proxyRoot.getTypes(),
                new TypeBridge.Converter(log, listenersFactory));
        users = new ConvertingListBridge<UserData, User<?, ?>, UserBridge>(
                log, listenersFactory, proxyRoot.getUsers(),
                new UserBridge.Converter(log, listenersFactory));

        addAutomation = new CommandBridge(log, listenersFactory, proxyRoot.getAddAutomationCommand());
        addDevice = new CommandBridge(log, listenersFactory, proxyRoot.getAddDeviceCommand());
        addHardware = new CommandBridge(log, listenersFactory, proxyRoot.getAddHardwareCommand());
        addUser = new CommandBridge(log, listenersFactory, proxyRoot.getAddUserCommand());

        renameCommand = new CommandBridge(log, listenersFactory, new RealCommand(log, listenersFactory, RENAME_ID, RENAME_ID, "Rename the client",
                Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, NAME_ID, NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(values != null && values.getChildren().containsKey(NAME_ID)) {
                    String newName = values.getChildren().get(NAME_ID).getFirstValue();
                    if (newName != null && !RealClientBridge.this.getData().getName().equals(newName)) {
                        RealClientBridge.this.getData().setName(newName);
                        RealClientBridge.this.broadcastMessage(NEW_NAME, new StringPayload(newName));
                        try {
                            TypeInstanceMap persistedValues = persistence.getValues(getPath());
                            persistedValues.getChildren().put(Renameable.NAME_ID, new TypeInstances(new TypeInstance(newName)));
                            persistence.saveValues(getPath(), persistedValues);
                        } catch(Throwable t) {
                            getLog().e("Failed to update persisted name", t);
                        }
                    }
                }
            }
        });

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

    private static RealClientData makeData(Log log, ClientInstance instance, Persistence persistence) {

        String id = instance.getApplicationDetails().getApplicationId() + "-" + instance.getApplicationInstanceId();
        String name = null;
        try {
            TypeInstanceMap persistedValues = persistence.getOrCreateValues(new String[]{"", RootBridge.REAL_CLIENTS_ID, id});
            TypeInstances nameValues = persistedValues.getChildren().get(Renameable.NAME_ID);
            if (nameValues != null)
                name = nameValues.getFirstValue();
        } catch(Throwable t) {
            log.e("Failed to load name for client " + id, t);
        }
        if(name == null)
            name = instance.getApplicationDetails().getApplicationName() + " - " + instance.getApplicationInstanceId();
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

    @Override
    public CommandBridge getRenameCommand() {
        return renameCommand;
    }
}
