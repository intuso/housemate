package com.intuso.housemate.client.real.impl.internal;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.utils.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealServerRoot extends RealServerImpl {

    private final Connection connection;
    private final RealNodeImpl.Factory nodeFactory;

    @Inject
    public RealServerRoot(@com.intuso.housemate.client.real.impl.internal.ioc.Server Logger logger,
                          ListenersFactory listenersFactory,
                          RealAutomationImpl.Factory automationFactory,
                          RealListPersistedImpl.Factory<RealAutomationImpl> automationsFactory,
                          RealDeviceImpl.Factory deviceFactory,
                          RealListPersistedImpl.Factory<RealDeviceImpl> devicesFactory,
                          RealListGeneratedImpl.Factory<RealNodeImpl> nodesFactory,
                          RealUserImpl.Factory userFactory,
                          RealListPersistedImpl.Factory<RealUserImpl> usersFactory,
                          RealNodeImpl.Factory nodeFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          Connection connection) throws JMSException {
        super(logger, "server", "server", "server", listenersFactory, automationFactory, automationsFactory,
                deviceFactory, devicesFactory, nodesFactory, userFactory, usersFactory, addAutomationCommandFactory,
                addDeviceCommandFactory, addUserCommandFactory);
        this.connection = connection;
        this.nodeFactory = nodeFactory;
    }

    public void start() {
        try {
            init("server", connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
        addNode(nodeFactory.create(ChildUtil.logger(logger, NODES_ID, "local"), "local", "Local", "Local Node"));
    }

    public void stop() {
        uninit();
    }

    public static class Service extends AbstractIdleService {

        private final RealServerRoot server;

        @Inject
        public Service(RealServerRoot server) {
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