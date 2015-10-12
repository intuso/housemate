package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.housemate.server.comms.ClientInstance;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerBridge
        extends BridgeObject<ServerData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
        ServerBridge, Server.Listener<? super ServerBridge>>
        implements Server<
                ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>,
                ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>,
                ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>,
                ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge>,
                ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge>,
                ConvertingListBridge<UserData, User<?, ?>, UserBridge>,
                    CommandBridge,
        ServerBridge> {

    private final ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge> applications;
    private final ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> automations;
    private final ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> devices;
    private final ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge> hardwares;
    private final ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge> types;
    private final ConvertingListBridge<UserData, User<?, ?>, UserBridge> users;
    private final CommandBridge addAutomation;
    private final CommandBridge addDevice;
    private final CommandBridge addHardware;
    private final CommandBridge addUser;

    private final CommandBridge renameCommand;

    public ServerBridge(Log log, ListenersFactory listenersFactory, ServerProxyRoot proxyRoot, final Persistence persistence) {
        super(log, listenersFactory, makeData(log, (ClientInstance.Application) proxyRoot.getClient().getClientInstance(), persistence));
        applications = new ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>(
                log, listenersFactory, proxyRoot.getApplications(),
                new ApplicationBridge.Converter(log, listenersFactory));
        automations = new ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>(
                log, listenersFactory, proxyRoot.getAutomations(),
                new AutomationBridge.Converter(log, listenersFactory));
        devices = new ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>(
                log, listenersFactory, proxyRoot.getDevices(),
                new DeviceBridge.Converter(log, listenersFactory));
        hardwares = new ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge>(
                log, listenersFactory, proxyRoot.getHardwares(),
                new HardwareBridge.Converter(log, listenersFactory));
        types = new ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge>(
                log, listenersFactory, proxyRoot.getTypes(),
                new TypeBridge.Converter(log, listenersFactory));
        users = new ConvertingListBridge<UserData, User<?, ?>, UserBridge>(
                log, listenersFactory, proxyRoot.getUsers(),
                new UserBridge.Converter(log, listenersFactory));

        addAutomation = new CommandBridge(log, listenersFactory, proxyRoot.getAddAutomationCommand());
        addDevice = new CommandBridge(log, listenersFactory, proxyRoot.getAddDeviceCommand());
        addHardware = new CommandBridge(log, listenersFactory, proxyRoot.getAddHardwareCommand());
        addUser = new CommandBridge(log, listenersFactory, proxyRoot.getAddUserCommand());

        renameCommand = null;/* todo use custom Command impl instead of RealCommand
                new CommandBridge(log, listenersFactory, new RealCommand(log, listenersFactory, ServerData.RENAME_ID, ServerData.RENAME_ID, "Rename the client",
                Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, ServerData.NAME_ID, ServerData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(ServerData.NAME_ID)) {
                    String newName = values.getChildren().get(ServerData.NAME_ID).getFirstValue();
                    if (newName != null && !ServerBridge.this.getData().getName().equals(newName)) {
                        ServerBridge.this.getData().setName(newName);
                        ServerBridge.this.broadcastMessage(ServerData.NEW_NAME, new StringPayload(newName));
                        try {
                            TypeInstanceMap persistedValues = persistence.getValues(getPath());
                            persistedValues.getChildren().put(ServerData.NAME_ID, new TypeInstances(new TypeInstance(newName)));
                            persistence.saveValues(getPath(), persistedValues);
                        } catch(Throwable t) {
                            getLog().e("Failed to update persisted name", t);
                        }
                    }
                }
            }
        });*/

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

    private static ServerData makeData(Log log, ClientInstance.Application instance, Persistence persistence) {

        String id = instance.getApplicationDetails().getApplicationId() + "-" + instance.getApplicationInstanceId();
        String name = null;
        try {
            TypeInstanceMap persistedValues = persistence.getOrCreateValues(new String[]{"", RootBridge.SERVERS_ID, id});
            TypeInstances nameValues = persistedValues.getChildren().get(ServerData.NAME_ID);
            if (nameValues != null)
                name = nameValues.getFirstValue();
        } catch(Throwable t) {
            log.e("Failed to load name for client " + id, t);
        }
        if(name == null)
            name = instance.getApplicationDetails().getApplicationName() + " - " + instance.getApplicationInstanceId();
        return new ServerData(id, name, name);
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
    public ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> getDevices() {
        return devices;
    }

    @Override
    public ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge> getTypes() {
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
