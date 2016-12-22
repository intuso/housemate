package com.intuso.housemate.client.real.impl.internal;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealAutomationImpl, RealListPersistedImpl<RealAutomationImpl>,
        RealDeviceImpl, RealListPersistedImpl<RealDeviceImpl>,
        RealUserImpl, RealListPersistedImpl<RealUserImpl>,
        ServerBaseNode<?, ?, ?, ?>, RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceCommand.Callback,
        AddUserCommand.Callback {

    private final Connection connection;
    private final RealNodeImpl.Factory nodeFactory;

    private final RealListPersistedImpl<RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<RealDeviceImpl> devices;
    private final RealCommandImpl addDeviceCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(Connection connection,
                          @com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ListenersFactory listenersFactory,
                          final RealAutomationImpl.Factory automationFactory,
                          RealListPersistedImpl.Factory<RealAutomationImpl> automationsFactory,
                          final RealDeviceImpl.Factory deviceFactory,
                          RealListPersistedImpl.Factory<RealDeviceImpl> devicesFactory,
                          RealNodeImpl.Factory nodeFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          final RealUserImpl.Factory userFactory,
                          RealListPersistedImpl.Factory<RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory) {
        super(logger, true, new Server.Data( "server", "server", "server"), listenersFactory);
        this.connection = connection;
        this.nodeFactory = nodeFactory;
        this.automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
                AUTOMATIONS_ID,
                "Automations",
                "Automations",
                new RealListPersistedImpl.ExistingObjectFactory<RealAutomationImpl>() {
                    @Override
                    public RealAutomationImpl create(Logger parentLogger, Object.Data data) {
                        return automationFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addAutomationCommand = addAutomationCommandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                ADD_AUTOMATION_ID,
                ADD_AUTOMATION_ID,
                "Add automation",
                this,
                this);
        this.devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID),
                DEVICES_ID,
                "Devices",
                "Devices",
                new RealListPersistedImpl.ExistingObjectFactory<RealDeviceImpl>() {
                    @Override
                    public RealDeviceImpl create(Logger parentLogger, Object.Data data) {
                        return deviceFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addDeviceCommand = addDeviceCommandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_ID),
                ChildUtil.logger(logger, ADD_DEVICE_ID),
                ADD_DEVICE_ID,
                ADD_DEVICE_ID,
                "Add device",
                this,
                this);
        this.users = usersFactory.create(ChildUtil.logger(logger, USERS_ID),
                USERS_ID,
                "Users",
                "Users",
                new RealListPersistedImpl.ExistingObjectFactory<RealUserImpl>() {
                    @Override
                    public RealUserImpl create(Logger parentLogger, Object.Data data) {
                        return userFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addUserCommand = addUserCommandFactory.create(ChildUtil.logger(logger, ADD_USER_ID),
                ChildUtil.logger(logger, ADD_USER_ID),
                ADD_USER_ID,
                ADD_USER_ID,
                "Add user",
                this,
                this);
        this.nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID),
                NODES_ID,
                "Nodes",
                "Nodes");
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID), connection);
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID), connection);
        devices.init(ChildUtil.name(name, DEVICES_ID), connection);
        addDeviceCommand.init(ChildUtil.name(name, ADD_DEVICE_ID), connection);
        users.init(ChildUtil.name(name, USERS_ID), connection);
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID), connection);
        nodes.init(ChildUtil.name(name, NODES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        automations.uninit();
        addAutomationCommand.uninit();
        devices.uninit();
        addDeviceCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    @Override
    public RealListPersistedImpl<RealAutomationImpl> getAutomations() {
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
    public final void removeAutomation(RealAutomationImpl realAutomation) {
        automations.remove(realAutomation.getId());
    }

    @Override
    public RealListPersistedImpl<RealDeviceImpl> getDevices() {
        return devices;
    }

    @Override
    public RealCommandImpl getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void addDevice(RealDeviceImpl device) {
        devices.add(device);
    }

    @Override
    public void removeDevice(RealDeviceImpl device) {
        devices.remove(device.getId());
    }

    @Override
    public RealListPersistedImpl<RealUserImpl> getUsers() {
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
    public void removeUser(RealUserImpl user) {
        users.remove(user.getId());
    }

    @Override
    public RealNodeListImpl getNodes() {
        return nodes;
    }

    @Override
    public void addNode(ServerBaseNode node) {
        nodes.add(node);
    }

    @Override
    public void removeNode(ServerBaseNode node) {
        nodes.remove(node.getId());
    }

    public void start() {
        try {
            // don't put "real" in the name - this way real and proxy link up together
            init("server", connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
        nodes.add(nodeFactory.create(ChildUtil.logger(logger, NODES_ID, "local"), "local", "Local", "Local Node"));
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