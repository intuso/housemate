package com.intuso.housemate.client.real.impl.internal;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddSystemCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealAutomationImpl, RealListPersistedImpl<RealAutomationImpl>,
        RealSystemImpl, RealListPersistedImpl<RealSystemImpl>,
        RealUserImpl, RealListPersistedImpl<RealUserImpl>,
        ServerBaseNode<?, ?, ?, ?>, RealNodeListImpl,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddSystemCommand.Callback,
        AddUserCommand.Callback {

    private final Connection connection;
    private final RealNodeImpl.Factory nodeFactory;

    private final RealListPersistedImpl<RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<RealSystemImpl> systems;
    private final RealCommandImpl addSystemCommand;
    private final RealNodeListImpl nodes;
    private final RealListPersistedImpl<RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @Inject
    public RealServerImpl(Connection connection,
                          @com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          final RealAutomationImpl.Factory automationFactory,
                          RealListPersistedImpl.Factory<RealAutomationImpl> automationsFactory,
                          final RealSystemImpl.Factory systemFactory,
                          RealListPersistedImpl.Factory<RealSystemImpl> systemsFactory,
                          RealNodeImpl.Factory nodeFactory,
                          RealNodeListImpl.Factory nodesFactory,
                          final RealUserImpl.Factory userFactory,
                          RealListPersistedImpl.Factory<RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddSystemCommand.Factory addSystemCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory) {
        super(logger, new Server.Data( "server", "server", "server"), managedCollectionFactory);
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
        this.systems = systemsFactory.create(ChildUtil.logger(logger, SYSTEMS_ID),
                SYSTEMS_ID,
                "Systems",
                "Systems",
                new RealListPersistedImpl.ExistingObjectFactory<RealSystemImpl>() {
                    @Override
                    public RealSystemImpl create(Logger parentLogger, Object.Data data) {
                        return systemFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addSystemCommand = addSystemCommandFactory.create(ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ChildUtil.logger(logger, ADD_SYSTEM_ID),
                ADD_SYSTEM_ID,
                "Add system",
                "Add system",
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
        systems.init(ChildUtil.name(name, SYSTEMS_ID), connection);
        addSystemCommand.init(ChildUtil.name(name, ADD_SYSTEM_ID), connection);
        users.init(ChildUtil.name(name, USERS_ID), connection);
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID), connection);
        nodes.init(ChildUtil.name(name, NODES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        automations.uninit();
        addAutomationCommand.uninit();
        systems.uninit();
        addSystemCommand.uninit();
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

    public RealListPersistedImpl<RealSystemImpl> getSYSTEMS() {
        return systems;
    }

    @Override
    public RealCommandImpl getAddSystemCommand() {
        return addSystemCommand;
    }

    @Override
    public void addSystem(RealSystemImpl system) {
        systems.add(system);
    }

    @Override
    public void removeSystem(RealSystemImpl system) {
        systems.remove(system.getId());
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