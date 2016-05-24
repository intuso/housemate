package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.real.api.internal.RealNode;
import com.intuso.housemate.client.real.impl.internal.ioc.Root;
import com.intuso.housemate.client.real.impl.internal.type.RegisteredTypes;
import com.intuso.housemate.client.real.impl.internal.utils.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public final class RealNodeImpl
        extends RealObject<Node.Data, Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealListImpl<RealTypeImpl<?>>, RealHardwareImpl, RealListImpl<RealHardwareImpl>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final RealListImpl<RealTypeImpl<?>> types;
    private final RealListImpl<RealHardwareImpl> hardwares;
    private final RealCommandImpl addHardwareCommand;

    @AssistedInject
    public RealNodeImpl(@Assisted Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        RealListImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, new Node.Data(id, name, description), listenersFactory);
        this.types = registeredTypes.createList(ChildUtil.logger(logger, TYPES_ID),
                TYPES_ID,
                "Types",
                "Types");
        this.hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                HARDWARES_ID,
                "Hardware",
                "Hardware",
                Lists.<RealHardwareImpl>newArrayList());
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                ChildUtil.logger(logger, ADD_HARDWARE_ID),
                ADD_HARDWARE_ID,
                ADD_HARDWARE_ID,
                "Add hardware",
                this,
                this);
    }

    @Inject
    public RealNodeImpl(@Root Logger logger,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        RealListImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        Connection connection) throws JMSException {
        this(logger, "node", "node", "node", listenersFactory, registeredTypes, hardwaresFactory, addHardwareCommandFactory);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.types.init(TYPES_ID, session);
        this.hardwares.init(HARDWARES_ID, session);
        this.addHardwareCommand.init(ADD_HARDWARE_ID, session);
    }

    @Override
    public RealListImpl<RealTypeImpl<?>> getTypes() {
        return types;
    }

    @Override
    public RealListImpl<RealHardwareImpl> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl hardware) {
        hardwares.add(hardware);
    }

    @Override
    public final void removeHardware(RealHardwareImpl realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
        return addHardwareCommand;
    }

    public interface Factory {
        RealNodeImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description);
    }
}