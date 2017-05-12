package com.intuso.housemate.client.real.impl.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.proxy.internal.object.ProxyServer;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddSystemCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealListPersistedImpl<Automation.Data, RealAutomationImpl>,
        RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl>,
        RealListPersistedImpl<User.Data, RealUserImpl>,
        RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddSystemCommand.Callback,
        AddUserCommand.Callback {

    private final ProxyServer.Simple proxyServer;
    private final RealValueImpl.Factory valueFactory;
    private final RealTypeImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>> deviceReferenceType;

    private final CombinationList<Device<?, ?, ?, ?, ?, ?>> devices;
    private final RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> deviceReferences;
    private final RealListPersistedImpl<Automation.Data, RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl> deviceGroups;
    private final RealCommandImpl addSystemCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<User.Data, RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(@com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          ProxyServer.Simple proxyServer,
                          TypeRepository typeRepository,
                          Sender.Factory senderFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> devicesFactory,
                          RealValueImpl.Factory valueFactory,
                          RealListPersistedImpl.Factory<Automation.Data, RealAutomationImpl> automationsFactory,
                          RealListPersistedImpl.Factory<Device.Group.Data, RealDeviceGroupImpl> deviceGroupsFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          RealListPersistedImpl.Factory<User.Data, RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddSystemCommand.Factory addSystemCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          RealNodeImpl node) {
        super(logger, new Server.Data( "server", "server", "server"), managedCollectionFactory, senderFactory);
        this.proxyServer = proxyServer;
        this.valueFactory = valueFactory;
        this.deviceReferenceType = typeRepository.getType(new TypeSpec(Types.newParameterizedType(ObjectReference.class, ProxyDevice.class)));
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
        this.deviceGroups = deviceGroupsFactory.create(ChildUtil.logger(logger, DEVICE_GROUPS_ID),
                DEVICE_GROUPS_ID,
                "Device Groups",
                "Device Groups");
        this.addSystemCommand = addSystemCommandFactory.create(ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ADD_SYSTEM_ID,
                "Add system",
                "Add system",
                this,
                deviceGroups.getRemoveCallback());
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
        this.devices = new CombinationList<>(new List.Data("device", "Devices", "Devices"), managedCollectionFactory);
        this.devices.addList(deviceGroups);
        nodes.addObjectListener(new NodeListListener(), true);
        nodes.add(node);
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        deviceReferences.init(ChildUtil.name(name, DEVICES_ID));
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID));
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID));
        deviceGroups.init(ChildUtil.name(name, DEVICE_GROUPS_ID));
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
        deviceGroups.uninit();
        addSystemCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    public RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public CombinationList<Device<?, ?, ?, ?, ?, ?>> getDevices() {
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

    public RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl> getDeviceGroups() {
        return deviceGroups;
    }

    @Override
    public RealCommandImpl getAddSystemCommand() {
        return addSystemCommand;
    }

    @Override
    public void addSystem(RealDeviceGroupImpl system) {
        deviceGroups.add(system);
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

    @Override
    public Object<?, ?> getChild(String id) {
        if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_SYSTEM_ID.equals(id))
            return addSystemCommand;
        else if(ADD_USER_ID.equals(id))
            return addUserCommand;
        else if(DEVICES_ID.equals(id))
            return deviceReferences;
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(DEVICE_GROUPS_ID.equals(id))
            return deviceGroups;
        else if(NODES_ID.equals(id))
            return nodes;
        else if(USERS_ID.equals(id))
            return users;
        return null;
    }

    public <T extends Object<?, ?>> T find(String[] path, boolean fail) {
        Object<?, ?> current = this;
        for(int i = 0; i < path.length; i++) {
            current = current.getChild(path[i]);
            if(current == null) {
                if(fail) {
                    if (i == 0)
                        throw new HousemateException("Could not find " + path[i] + " for server");
                    else {
                        String[] subPath = new String[i];
                        System.arraycopy(path, 0, subPath, 0, i);
                        throw new HousemateException("Could not find " + path[i] + " at " + Joiner.on("/").join(subPath));
                    }
                } else
                    return null;
            }
        }
        return (T) current;
    }

    private class NodeListListener implements List.Listener<Node<?, ?, ?, ?>, RealNodeListImpl> {

        @Override
        public void elementAdded(RealNodeListImpl list, Node<?, ?, ?, ?> node) {
            node.getHardwares().addObjectListener(new HardwareListListener(node.getId()), true);
        }

        @Override
        public void elementRemoved(RealNodeListImpl list, Node<?, ?, ?, ?> node) {
            // todo remove the hardwares listener
            // todo remove all devices from the references list
        }
    }

    private class HardwareListListener implements List.Listener<Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final String nodeId;

        private HardwareListListener(String nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public void elementAdded(List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?> list, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            RealServerImpl.this.devices.addList(hardware.getDeviceConnecteds());
            hardware.getDeviceConnecteds().addObjectListener(new DeviceListListener(nodeId, hardware.getId()), true);
        }

        @Override
        public void elementRemoved(List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?> list, Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            // todo remove hardware devices list from the server devices combination list.
            // todo remove the devices listener
            // todo remove all devices from the references list
        }
    }

    private class DeviceListListener implements List.Listener<Device<?, ?, ?, ?, ?, ?>, List<? extends Device<?, ?, ?, ?, ?, ?>, ?>> {

        private final String nodeId;
        private final String hardwareId;

        private DeviceListListener(String nodeId, String hardwareId) {
            this.nodeId = nodeId;
            this.hardwareId = hardwareId;
        }

        @Override
        public void elementAdded(List<? extends Device<?, ?, ?, ?, ?, ?>, ?> list, Device<?, ?, ?, ?, ?, ?> device) {
            deviceReferences.add((RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>) valueFactory.create(
                    ChildUtil.logger(logger, DEVICES_ID, device.getId()),
                    device.getId(),
                    device.getName(),
                    device.getDescription(),
                    deviceReferenceType,
                    1,
                    1,
                    Lists.newArrayList(proxyServer.reference(new String[] {NODES_ID, nodeId, Node.HARDWARES_ID, hardwareId, Hardware.DEVICES_ID, device.getId()}))));
        }

        @Override
        public void elementRemoved(List<? extends Device<?, ?, ?, ?, ?, ?>, ?> list, Device<?, ?, ?, ?, ?, ?> device) {
            deviceReferences.remove(device.getId());
        }
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