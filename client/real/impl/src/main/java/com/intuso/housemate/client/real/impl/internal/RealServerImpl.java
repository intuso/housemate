package com.intuso.housemate.client.real.impl.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.messaging.api.internal.ioc.Messaging;
import com.intuso.housemate.client.proxy.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddDeviceGroupCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.housemate.client.v1_0.messaging.jms.JMS;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>, ServerView>
        implements RealServer<RealCommandImpl,
        RealListPersistedImpl<Automation.Data, RealAutomationImpl>,
        RealListGeneratedImpl<RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>>,
        RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl>,
        RealListPersistedImpl<User.Data, RealUserImpl>,
        RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceGroupCommand.Callback,
        AddUserCommand.Callback {

    private final Sender.Factory senderFactory;
    private final Receiver.Factory receiverFactory;

    private final RealReferenceImpl.Factory referenceFactory;

    private final RealListPersistedImpl<Automation.Data, RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListGeneratedImpl<RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>> devices;
    private final RealListPersistedImpl<Device.Group.Data, RealDeviceGroupImpl> deviceGroups;
    private final RealCommandImpl addDeviceGroupCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<User.Data, RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(@com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          ProxyServer.Simple proxyServer,
                          @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE) Sender.Factory senderFactory,
                          @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE) Receiver.Factory receiverFactory,
                          RealReferenceImpl.Factory referenceFactory,
                          RealListPersistedImpl.Factory<Automation.Data, RealAutomationImpl> automationsFactory,
                          RealListGeneratedImpl.Factory<RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>> devicesFactory,
                          RealListPersistedImpl.Factory<Device.Group.Data, RealDeviceGroupImpl> deviceGroupsFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          RealListPersistedImpl.Factory<User.Data, RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceGroupCommand.Factory addDeviceGroupCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          RealNodeImpl node) {
        super(logger, new Server.Data( "server", "server", "server"), managedCollectionFactory);
        this.senderFactory = senderFactory;
        this.receiverFactory = receiverFactory;
        this.referenceFactory = referenceFactory;
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
        this.devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID),
                DEVICES_ID,
                "Devices",
                "Devices",
                Lists.newArrayList());
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
        proxyServer.load(new ServerView(View.Mode.SELECTION)
                .setDeviceGroups(new ListView<>(View.Mode.CHILDREN))
                .setNodes(new ListView<NodeView>(View.Mode.CHILDREN)
                        .setView(new NodeView(View.Mode.SELECTION)
                                .setHardwares(new ListView<HardwareView>(View.Mode.CHILDREN)
                                        .setView(new HardwareView(View.Mode.SELECTION)
                                                .setDevices(new ListView<>(View.Mode.CHILDREN)))))));
        addDeviceList("groups", proxyServer.getDeviceGroups());
        proxyServer.getNodes().addObjectListener(new NodeListListener(), true);
        nodes.add(node);
    }

    @Override
    public ServerView createView(View.Mode mode) {
        return new ServerView(mode);
    }

    @Override
    public Tree getTree(ServerView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(USERS_ID, users.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(NODES_ID, nodes.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomations(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroups(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(USERS_ID, users.getTree(view.getUsers(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(NODES_ID, nodes.getTree(view.getNodes(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getAutomations() != null)
                        result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomations(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddAutomationCommand() != null)
                        result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDevices() != null)
                        result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDeviceGroups() != null)
                        result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroups(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddDeviceGroupCommand() != null)
                        result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getUsers() != null)
                        result.getChildren().put(USERS_ID, users.getTree(view.getUsers(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddUserCommand() != null)
                        result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getNodes() != null)
                        result.getChildren().put(NODES_ID, nodes.getTree(view.getNodes(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID), senderFactory, receiverFactory);
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID), senderFactory, receiverFactory);
        devices.init(ChildUtil.name(name, DEVICES_ID), senderFactory, receiverFactory);
        deviceGroups.init(ChildUtil.name(name, DEVICE_GROUPS_ID), senderFactory, receiverFactory);
        addDeviceGroupCommand.init(ChildUtil.name(name, ADD_DEVICE_GROUP_ID), senderFactory, receiverFactory);
        users.init(ChildUtil.name(name, USERS_ID), senderFactory, receiverFactory);
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID), senderFactory, receiverFactory);
        nodes.init(ChildUtil.name(name, NODES_ID), senderFactory, receiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        automations.uninit();
        addAutomationCommand.uninit();
        devices.uninit();
        deviceGroups.uninit();
        addDeviceGroupCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
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

    @Override
    public RealListGeneratedImpl<RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>> getDevices() {
        return devices;
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
        init("server", senderFactory, receiverFactory);
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
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(DEVICES_ID.equals(id))
            return devices;
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
                        throw new HousemateException("Could not find " + path[i] + " at " + Joiner.on(".").join(subPath));
                    }
                } else
                    return null;
            }
        }
        return (T) current;
    }

    private void addDeviceList(String idPrefix, ProxyList<? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?> list) {
        list.addObjectListener(new DeviceListListener(idPrefix), true);
    }

    private class NodeListListener implements List.Listener<ProxyNode<?, ?, ?, ?>, ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>> {

        @Override
        public void elementAdded(ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?> list, ProxyNode<?, ?, ?, ?> node) {
            node.getHardwares().addObjectListener(new HardwareListListener(node.getId()), true);
        }

        @Override
        public void elementRemoved(ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?> list, ProxyNode<?, ?, ?, ?> node) {
            // todo remove the hardwares listener
            // todo remove all devices from the references list
        }
    }

    private class HardwareListListener implements List.Listener<ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?>, ProxyList<? extends ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final String nodeId;

        private HardwareListListener(String nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public void elementAdded(ProxyList<? extends ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?>, ?> list, ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            addDeviceList(nodeId + "-" + hardware.getId(), hardware.getDeviceConnecteds());
        }

        @Override
        public void elementRemoved(ProxyList<? extends ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?>, ?> list, ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            // todo remove hardware devices list from the server devices combination list.
            // todo remove the devices listener
            // todo remove all devices from the references list
        }
    }

    private class DeviceListListener implements List.Listener<ProxyDevice<?, ?, ?, ?, ?, ?>, ProxyList<? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?>> {

        private final String idPrefix;

        private DeviceListListener(String idPrefix) {
            this.idPrefix = idPrefix + "-";
        }

        @Override
        public void elementAdded(ProxyList<? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?> list, ProxyDevice<?, ?, ?, ?, ?, ?> device) {
            devices.add(referenceFactory.create(
                    ChildUtil.logger(logger, DEVICES_ID, device.getId()),
                    idPrefix + device.getId(),
                    device.getName(),
                    device.getDescription(),
                    device.getPath()));
        }

        @Override
        public void elementRemoved(ProxyList<? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?> list, ProxyDevice<?, ?, ?, ?, ?, ?> device) {
            devices.remove(idPrefix + device.getId());
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