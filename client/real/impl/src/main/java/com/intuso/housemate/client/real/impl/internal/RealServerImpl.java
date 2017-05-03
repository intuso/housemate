package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddSystemCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealListPersistedImpl<Automation.Data, RealAutomationImpl>,
        RealListPersistedImpl<Device.Combi.Data, RealDeviceCombiImpl>,
        RealListPersistedImpl<User.Data, RealUserImpl>,
        RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddSystemCommand.Callback,
        AddUserCommand.Callback {

    private final CombinationList<Device<?, ?, ?, ?, ?>> devices;
    private final RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> deviceReferences;
    private final RealListPersistedImpl<Automation.Data, RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<Device.Combi.Data, RealDeviceCombiImpl> deviceCombis;
    private final RealCommandImpl addSystemCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<User.Data, RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(@com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> devicesFactory,
                          RealListPersistedImpl.Factory<Automation.Data, RealAutomationImpl> automationsFactory,
                          RealListPersistedImpl.Factory<Device.Combi.Data, RealDeviceCombiImpl> deviceCombisFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          RealListPersistedImpl.Factory<User.Data, RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddSystemCommand.Factory addSystemCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          RealNodeImpl node) {
        super(logger, new Server.Data( "server", "server", "server"), managedCollectionFactory, senderFactory);
        this.deviceReferences = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID),
                DEVICES_ID,
                "Devices",
                "Devices",
                Lists.newArrayList());
        this.automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
                AUTOMATIONS_ID,
                "Automations",
                "Automations");
        this.addAutomationCommand = addAutomationCommandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                ADD_AUTOMATION_ID,
                ADD_AUTOMATION_ID,
                "Add automation",
                this,
                automations.getRemoveCallback());
        this.deviceCombis = deviceCombisFactory.create(ChildUtil.logger(logger, DEVICE_COMBIS_ID),
                DEVICE_COMBIS_ID,
                "Device Combis",
                "Device Combis");
        this.addSystemCommand = addSystemCommandFactory.create(ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ADD_SYSTEM_ID,
                "Add system",
                "Add system",
                this,
                deviceCombis.getRemoveCallback());
        this.users = usersFactory.create(ChildUtil.logger(logger, USERS_ID),
                USERS_ID,
                "Users",
                "Users");
        this.addUserCommand = addUserCommandFactory.create(ChildUtil.logger(logger, ADD_USER_ID),
                ChildUtil.logger(logger, ADD_USER_ID),
                ADD_USER_ID,
                ADD_USER_ID,
                "Add user",
                this,
                users.getRemoveCallback());
        this.nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID),
                NODES_ID,
                "Nodes",
                "Nodes");
        this.devices = new CombinationList<>("device", "Devices", "Devices", managedCollectionFactory);
        this.devices.addList(deviceCombis);
        // todo, listener to nodes, hardwares, devices, and all them all to this
        nodes.add(node);
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        deviceReferences.init(ChildUtil.name(name, DEVICES_ID));
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID));
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID));
        deviceCombis.init(ChildUtil.name(name, DEVICE_COMBIS_ID));
        addSystemCommand.init(ChildUtil.name(name, ADD_SYSTEM_ID));
        users.init(ChildUtil.name(name, USERS_ID));
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID));
        nodes.init(ChildUtil.name(name, NODES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        deviceReferences.uninit();
        automations.uninit();
        addAutomationCommand.uninit();
        deviceCombis.uninit();
        addSystemCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    public RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public CombinationList<Device<?, ?, ?, ?, ?>> getDevices() {
        return devices;
    }

    @Override
    public RealListPersistedImpl<Automation.Data, RealAutomationImpl> getAutomations() {
        return automations;
    }

    @Override
    public RealCommandImpl getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public final void addAutomation(RealAutomationImpl automation) {
        automations.add(automation);
    }

    public RealListPersistedImpl<Device.Combi.Data, RealDeviceCombiImpl> getDeviceCombis() {
        return deviceCombis;
    }

    @Override
    public RealCommandImpl getAddSystemCommand() {
        return addSystemCommand;
    }

    @Override
    public void addSystem(RealDeviceCombiImpl system) {
        deviceCombis.add(system);
    }

    @Override
    public RealListPersistedImpl<User.Data, RealUserImpl> getUsers() {
        return users;
    }

    @Override
    public RealCommandImpl getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public void addUser(RealUserImpl user) {
        users.add(user);
    }

    @Override
    public RealNodeListImpl getNodes() {
        return nodes;
    }

    public void start() {
        // don't put "real" in the name - this way real and proxy link up together
        init("server");
    }

    public void stop() {
        uninit();
    }

    public static class Service extends AbstractIdleService {

        private final RealServerImpl server;

        @Inject
        public Service(RealServerImpl server) {
            this.server = server;
        }

        @Override
        protected void startUp() throws Exception {
            server.start();
        }

        @Override
        protected void shutDown() throws Exception {
            server.stop();
        }
    }
}