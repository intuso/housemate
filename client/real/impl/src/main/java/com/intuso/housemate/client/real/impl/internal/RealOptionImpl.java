package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

public final class RealOptionImpl
        extends RealObject<Option.Data, Option.Listener<? super RealOptionImpl>>
        implements RealOption<RealListImpl<RealSubTypeImpl<?>>, RealOptionImpl> {

    private final RealListImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param subTypes the option's sub types
     * @param listenersFactory
     */
    @Inject
    public RealOptionImpl(@Assisted Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted Iterable<RealSubTypeImpl<?>> subTypes,
                          ListenersFactory listenersFactory,
                          RealListImpl.Factory<RealSubTypeImpl<?>> subTypesFactory) {
        super(logger, new Option.Data(id, name, description), listenersFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID),
                Option.SUB_TYPES_ID,
                "Sub Types",
                "The sub types of this option",
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

    public interface Factory {
        RealOptionImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              Iterable<RealSubTypeImpl<?>> subTypes);
    }
}
