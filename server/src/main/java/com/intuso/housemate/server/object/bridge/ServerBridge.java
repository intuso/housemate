package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.housemate.server.comms.ClientInstance;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerBridge
        extends BridgeObject<ServerData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
        ServerBridge, Server.Listener<? super ServerBridge>>
        implements Server<
                ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>,
                ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>,
                ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>,
                ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge>,
                ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge>,
                ConvertingListBridge<UserData, User<?, ?, ?>, UserBridge>,
                    CommandBridge,
        ServerBridge> {

    private final ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge> applications;
    private final ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> automations;
    private final ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> devices;
    private final ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge> hardwares;
    private final ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge> types;
    private final ConvertingListBridge<UserData, User<?, ?, ?>, UserBridge> users;
    private final CommandBridge addAutomation;
    private final CommandBridge addDevice;
    private final CommandBridge addHardware;
    private final CommandBridge addUser;

    private final CommandBridge renameCommand;

    public ServerBridge(Logger logger, ListenersFactory listenersFactory, ServerProxyRoot proxyRoot, final Persistence persistence) {
        super(listenersFactory, logger, makeData(logger, (ClientInstance.Application) proxyRoot.getClient().getClientInstance(), persistence));

        if(proxyRoot.getApplications() != null) {
            applications = new ConvertingListBridge<ApplicationData, Application<?, ?, ?, ?, ?>, ApplicationBridge>(
                    logger, listenersFactory, proxyRoot.getApplications(),
                    new ApplicationBridge.Converter(logger, listenersFactory));
            addChild(applications);
        } else
            applications = null;

        if(proxyRoot.getAutomations() != null) {
            automations = new ConvertingListBridge<AutomationData, Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge>(
                    logger, listenersFactory, proxyRoot.getAutomations(),
                    new AutomationBridge.Converter(logger, listenersFactory));
            addChild(automations);
        } else
            automations = null;

        if(proxyRoot.getAutomations() != null) {
            devices = new ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge>(
                    logger, listenersFactory, proxyRoot.getDevices(),
                    new DeviceBridge.Converter(logger, listenersFactory));
            addChild(devices);
        } else
            devices = null;

        if(proxyRoot.getAutomations() != null) {
            hardwares = new ConvertingListBridge<HardwareData, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HardwareBridge>(
                    logger, listenersFactory, proxyRoot.getHardwares(),
                    new HardwareBridge.Converter(logger, listenersFactory));
            addChild(hardwares);
        } else
            hardwares = null;

        if(proxyRoot.getAutomations() != null) {
            types = new ConvertingListBridge<TypeData<?>, Type<?>, TypeBridge>(
                    logger, listenersFactory, proxyRoot.getTypes(),
                    new TypeBridge.Converter(logger, listenersFactory));
            addChild(types);
        } else
            types = null;

        if(proxyRoot.getAutomations() != null) {
            users = new ConvertingListBridge<UserData, User<?, ?, ?>, UserBridge>(
                    logger, listenersFactory, proxyRoot.getUsers(),
                    new UserBridge.Converter(logger, listenersFactory));
            addChild(users);
        } else
            users = null;

        if(proxyRoot.getAddAutomationCommand() != null) {
            addAutomation = new CommandBridge(logger, listenersFactory, proxyRoot.getAddAutomationCommand());
            addChild(addAutomation);
        } else
            addAutomation = null;

        if(proxyRoot.getAddDeviceCommand() != null) {
            addDevice = new CommandBridge(logger, listenersFactory, proxyRoot.getAddDeviceCommand());
            addChild(addDevice);
        } else
            addDevice = null;

        if(proxyRoot.getAddHardwareCommand() != null) {
            addHardware = new CommandBridge(logger, listenersFactory, proxyRoot.getAddHardwareCommand());
            addChild(addHardware);
        } else
            addHardware = null;

        if(proxyRoot.getAddUserCommand() != null) {
            addUser = new CommandBridge(logger, listenersFactory, proxyRoot.getAddUserCommand());
            addChild(addUser);
        } else
            addUser = null;

        renameCommand = null;/* todo use custom Command impl instead of RealCommand
                new CommandBridge(logger, listenersFactory, new RealCommand(logger, listenersFactory, ServerData.RENAME_ID, ServerData.RENAME_ID, "Rename the client",
                Lists.<RealParameter<?>>newArrayList(StringType.createParameter(logger, listenersFactory, ServerData.NAME_ID, ServerData.NAME_ID, "The new name"))) {
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
                            getLog().error("Failed to update persisted name", t);
                        }
                    }
                }
            }
        });*/
    }

    private static ServerData makeData(Logger logger, ClientInstance.Application instance, Persistence persistence) {

        String id = instance.getApplicationDetails().getApplicationId() + "-" + instance.getApplicationInstanceId();
        String name = null;
        try {
            TypeInstanceMap persistedValues = persistence.getOrCreateValues(new String[]{"", RootBridge.SERVERS_ID, id});
            TypeInstances nameValues = persistedValues.getChildren().get(ServerData.NAME_ID);
            if (nameValues != null)
                name = nameValues.getFirstValue();
        } catch(Throwable t) {
            logger.error("Failed to load name for client " + id, t);
        }
        if(name == null)
            name = instance.getApplicationDetails().getApplicationName();
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
    public ConvertingListBridge<DeviceData, Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, DeviceBridge> getDevices() {
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
    public ConvertingListBridge<UserData, User<?, ?, ?>, UserBridge> getUsers() {
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
