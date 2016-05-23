package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.internal.ioc.Root;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public final class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealAutomationImpl, RealListImpl<RealAutomationImpl>,
        RealDeviceImpl, RealListImpl<RealDeviceImpl>,
        RealUserImpl, RealListImpl<RealUserImpl>,
        RealNodeImpl, RealListImpl<RealNodeImpl>,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceCommand.Callback,
        AddUserCommand.Callback {

    private final RealListImpl<RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListImpl<RealDeviceImpl> devices;
    private final RealCommandImpl addDeviceCommand;
    private final RealListImpl<RealUserImpl> users;
    private final RealCommandImpl addUserCommand;
    private final RealListImpl<RealNodeImpl> nodes;

    @AssistedInject
    private RealServerImpl(@Assisted Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           ListenersFactory listenersFactory,
                           RealListImpl.Factory<RealAutomationImpl> automationsFactory,
                           RealListImpl.Factory<RealDeviceImpl> devicesFactory,
                           RealListImpl.Factory<RealNodeImpl> nodesFactory,
                           RealListImpl.Factory<RealUserImpl> usersFactory,
                           AddAutomationCommand.Factory addAutomationCommandFactory,
                           AddDeviceCommand.Factory addDeviceCommandFactory,
                           AddUserCommand.Factory addUserCommandFactory) {
        super(logger, new Server.Data(id, name, description), listenersFactory);
        this.automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
                AUTOMATIONS_ID,
                "Automations",
                "Automations",
                Lists.<RealAutomationImpl>newArrayList());
        this.addAutomationCommand = addAutomationCommandFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
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
                Lists.<RealDeviceImpl>newArrayList());
        this.addDeviceCommand = addDeviceCommandFactory.create(ChildUtil.logger(logger, DEVICES_ID),
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
                Lists.<RealUserImpl>newArrayList());
        this.addUserCommand = addUserCommandFactory.create(ChildUtil.logger(logger, USERS_ID),
                ChildUtil.logger(logger, ADD_USER_ID),
                ADD_USER_ID,
                ADD_USER_ID,
                "Add user",
                this,
                this);
        this.nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID),
                NODES_ID,
                "Nodes",
                "Nodes",
                Lists.<RealNodeImpl>newArrayList());
    }

    @Inject
    public RealServerImpl(@Root Logger logger,
                          ListenersFactory listenersFactory,
                          RealListImpl.Factory<RealAutomationImpl> automationsFactory,
                          RealListImpl.Factory<RealDeviceImpl> devicesFactory,
                          RealListImpl.Factory<RealNodeImpl> nodesFactory,
                          RealListImpl.Factory<RealUserImpl> usersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          Connection connection) throws JMSException {
        this(logger, "server", "server", "server", listenersFactory, automationsFactory, devicesFactory, nodesFactory,
                usersFactory, addAutomationCommandFactory, addDeviceCommandFactory, addUserCommandFactory);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.automations.init(AUTOMATIONS_ID, session);
        this.addAutomationCommand.init(ADD_AUTOMATION_ID, session);
        this.devices.init(DEVICES_ID, session);
        this.addDeviceCommand.init(ADD_DEVICE_ID, session);
        this.users.init(USERS_ID, session);
        this.addUserCommand.init(ADD_USER_ID, session);
        this.nodes.init(NODES_ID, session);
    }

    @Override
    public RealListImpl<RealAutomationImpl> getAutomations() {
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
    public RealListImpl<RealDeviceImpl> getDevices() {
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
    public RealListImpl<RealUserImpl> getUsers() {
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
    public RealListImpl<RealNodeImpl> getNodes() {
        return nodes;
    }

    @Override
    public void addNode(RealNodeImpl node) {
        nodes.add(node);
    }

    @Override
    public void removeNode(RealNodeImpl node) {
        nodes.remove(node.getId());
    }

    public interface Factory {
        RealServerImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description);
    }
}