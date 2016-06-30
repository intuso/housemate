package com.intuso.housemate.client.real.impl.internal;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.real.impl.internal.ioc.Node;
import com.intuso.housemate.client.real.impl.internal.type.RegisteredTypes;
import com.intuso.housemate.client.real.impl.internal.utils.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealNodeRoot extends RealNodeImpl {

    private final String id;
    private final Connection connection;

    @Inject
    public RealNodeRoot(@Node Logger logger,
                        @Node String id,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        RealHardwareImpl.Factory hardwareFactory,
                        RealListPersistedImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        Connection connection) {
        super(logger, "node", "node", "node", listenersFactory, registeredTypes, hardwareFactory, hardwaresFactory, addHardwareCommandFactory);
        this.id = id;
        this.connection = connection;
    }

    public void start() {
        try {
            init(ChildUtil.name(null, Object.VERSION, Server.NODES_ID, id), connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
    }

    public void stop() {
        uninit();
    }

    public static class Service extends AbstractIdleService {

        private final RealNodeRoot node;

        @Inject
        public Service(RealNodeRoot node) {
            this.node = node;
        }

        @Override
        protected void startUp() throws Exception {
            node.start();
        }

        @Override
        protected void shutDown() throws Exception {
            node.stop();
        }
    }
}