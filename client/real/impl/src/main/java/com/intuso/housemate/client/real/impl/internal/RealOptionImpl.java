package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Arrays;
import java.util.List;

public final class RealOptionImpl
        extends RealObject<Option.Data, Option.Listener<? super RealOptionImpl>>
        implements RealOption<RealListImpl<RealSubTypeImpl<?>>, RealOptionImpl> {

    private final RealListImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger,
                          Option.Data data,
                          ListenersFactory listenersFactory,
                          RealSubTypeImpl<?>... subTypes) {
        this(logger, data, listenersFactory, Arrays.asList(subTypes));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger,
                          Option.Data data,
                          ListenersFactory listenersFactory,
                          List<RealSubTypeImpl<?>> subTypes) {
        super(logger, data, listenersFactory);
        this.subTypes = new RealListImpl<>(ChildUtil.logger(logger, Option.SUB_TYPES_ID),
                new com.intuso.housemate.client.api.internal.object.List.Data(Option.SUB_TYPES_ID, "Sub Types", "The sub types of this option"),
                listenersFactory,
                subTypes);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        subTypes.init(ChildUtil.name(name, Option.SUB_TYPES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public final RealListImpl<RealSubTypeImpl<?>> getSubTypes() {
        return subTypes;
    }
}
