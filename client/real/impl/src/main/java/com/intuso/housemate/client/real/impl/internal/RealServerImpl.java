package com.intuso.housemate.client.real.impl.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.ServerView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.proxy.internal.object.ProxyServer;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddDeviceGroupCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>, ServerView>
        implements RealServer<RealCommandImpl,
        RealListPersistedImpl<Automation.Data, RealAutomationImpl>,
        RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl>,
        RealListPersistedImpl<User.Data, RealUserImpl>,
        RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceGroupCommand.Callback,
        AddUserCommand.Callback {

    private final ProxyServer.Simple proxyServer;
    private final RealValueImpl.Factory valueFactory;
    private final RealTypeImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>> deviceReferenceType;

    private final CombinationList<Device<?, ?, ?, ?, ?, ?, ?>> devices;
    private final RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> deviceReferences;
    private final RealListPersistedImpl<Automation.Data, RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl> deviceGroups;
    private final RealCommandImpl addDeviceGroupCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<User.Data, RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(@com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          ProxyServer.Simple proxyServer,
                          TypeRepository typeRepository,
                          Sender.Factory senderFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> devicesFactory,
                          RealValueImpl.Factory valueFactory,
                          RealListPersistedImpl.Factory<Automation.Data, RealAutomationImpl> automationsFactory,
                          RealListPersistedImpl.Factory<Device.Group.Data, RealDeviceGroupImpl> deviceGroupsFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          RealListPersistedImpl.Factory<User.Data, RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceGroupCommand.Factory addDeviceGroupCommandFactory,
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
        this.addDeviceGroupCommand = addDeviceGroupCommandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_GROUP_ID),
                ChildUtil.logger(logger, ADD_DEVICE_GROUP_ID),
                ADD_DEVICE_GROUP_ID,
                "Add device group",
                "Add device group",
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
    public ServerView createView(View.Mode mode) {
        return new ServerView(mode);
    }

    @Override
    public Tree getTree(ServerView view, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(USERS_ID, users.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(NODES_ID, nodes.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomations(), listener, listenerRegistrations));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommand(), listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroups(), listener, listenerRegistrations));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommand(), listener, listenerRegistrations));
                    result.getChildren().put(USERS_ID, users.getTree(view.getUsers(), listener, listenerRegistrations));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommand(), listener, listenerRegistrations));
                    result.getChildren().put(NODES_ID, nodes.getTree(view.getNodes(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getAutomations() != null)
                        result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomations(), listener, listenerRegistrations));
                    if(view.getAddAutomationCommand() != null)
                        result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommand(), listener, listenerRegistrations));
                    if(view.getDevices() != null)
                        result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener, listenerRegistrations));
                    if(view.getDeviceGroups() != null)
                        result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroups(), listener, listenerRegistrations));
                    if(view.getAddDeviceGroupCommand() != null)
                        result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommand(), listener, listenerRegistrations));
                    if(view.getUsers() != null)
                        result.getChildren().put(USERS_ID, users.getTree(view.getUsers(), listener, listenerRegistrations));
                    if(view.getAddUserCommand() != null)
                        result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommand(), listener, listenerRegistrations));
                    if(view.getNodes() != null)
                        result.getChildren().put(NODES_ID, nodes.getTree(view.getNodes(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        deviceReferences.init(ChildUtil.name(name, DEVICES_ID));
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID));
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID));
        deviceGroups.init(ChildUtil.name(name, DEVICE_GROUPS_ID));
        addDeviceGroupCommand.init(ChildUtil.name(name, ADD_DEVICE_GROUP_ID));
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
        addDeviceGroupCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    public RealListGeneratedImpl<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public CombinationList<Device<?, ?, ?, ?, ?, ?, ?>> getDevices() {
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

    public RealCommandImpl getAddDeviceGroupCommand() {
        return addDeviceGroupCommand;
    }

    @Override
    public void addDeviceGroup(RealDeviceGroupImpl deviceGroup) {
        deviceGroups.add(deviceGroup);
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
    public Object<?, ?, ?> getChild(String id) {
        if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_DEVICE_GROUP_ID.equals(id))
            return addDeviceGroupCommand;
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

    public <T extends Object<?, ?, ?>> T find(String[] path, boolean fail) {
        Object<?, ?, ?> current = this;
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

    private class DeviceListListener implements List.Listener<Device<?, ?, ?, ?, ?, ?, ?>, List<? extends Device<?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final String nodeId;
        private final String hardwareId;

        private DeviceListListener(String nodeId, String hardwareId) {
            this.nodeId = nodeId;
            this.hardwareId = hardwareId;
        }

        @Override
        public void elementAdded(List<? extends Device<?, ?, ?, ?, ?, ?, ?>, ?> list, Device<?, ?, ?, ?, ?, ?, ?> device) {
            deviceReferences.add((RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>) valueFactory.create(
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
        public void elementRemoved(List<? extends Device<?, ?, ?, ?, ?, ?, ?>, ?> list, Device<?, ?, ?, ?, ?, ?, ?> device) {
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