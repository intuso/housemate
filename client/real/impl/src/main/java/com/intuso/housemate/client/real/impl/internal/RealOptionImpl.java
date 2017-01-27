package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealOptionImpl
        extends RealObject<Option.Data, Option.Listener<? super RealOptionImpl>>
        implements RealOption<RealListGeneratedImpl<RealSubTypeImpl<?>>, RealOptionImpl> {

    private final RealListGeneratedImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param subTypes the option's sub types
     * @param managedCollectionFactory
     */
    @Inject
    public RealOptionImpl(@Assisted Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted Iterable<RealSubTypeImpl<?>> subTypes,
                          ManagedCollectionFactory managedCollectionFactory,
                          RealListGeneratedImpl.Factory<RealSubTypeImpl<?>> subTypesFactory) {
        super(logger, new Option.Data(id, name, description), managedCollectionFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID),
                Option.SUB_TYPES_ID,
                "Sub Types",
                "The sub types of this option",
                subTypes);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        subTypes.init(ChildUtil.name(name, Option.SUB_TYPES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public final RealListGeneratedImpl<RealSubTypeImpl<?>> getSubTypes() {
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
