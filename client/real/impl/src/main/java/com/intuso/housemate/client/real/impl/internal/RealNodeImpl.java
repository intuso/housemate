package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.real.api.internal.RealNode;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public final class RealNodeImpl
        extends RealObject<Node.Data, Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealHardwareImpl<?>, RealListImpl<RealHardwareImpl<?>>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final RealListImpl<RealHardwareImpl<?>> hardwares;
    private final RealCommandImpl addHardwareCommand;

    private RealNodeImpl(Logger logger,
                         Node.Data data,
                         ListenersFactory listenersFactory,
                         AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, data, listenersFactory);
        this.hardwares = new RealListImpl<>(ChildUtil.logger(logger, HARDWARES_ID), new List.Data(HARDWARES_ID, "Hardware", "Hardware"), listenersFactory);
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(logger, ADD_HARDWARE_ID),
                new Command.Data(ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware"),
                this,
                this);
    }

    public RealNodeImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, AddHardwareCommand.Factory addHardwareCommandFactory) {
        this(logger, new Node.Data(id, name, description), listenersFactory, addHardwareCommandFactory);
    }

    @Inject
    public RealNodeImpl(ListenersFactory listenersFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        Connection connection) throws JMSException {
        this(LoggerFactory.getLogger("com.intuso.housemate.node"), new Node.Data("node", "node", "node"), listenersFactory, addHardwareCommandFactory);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.hardwares.init(HARDWARES_ID, session);
        this.addHardwareCommand.init(ADD_HARDWARE_ID, session);
    }

    @Override
    public RealListImpl<RealHardwareImpl<?>> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl<?> hardware) {
        hardwares.add(hardware);
    }

    @Override
    public final void removeHardware(RealHardwareImpl<?> realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
      return addHardwareCommand;
    }
}