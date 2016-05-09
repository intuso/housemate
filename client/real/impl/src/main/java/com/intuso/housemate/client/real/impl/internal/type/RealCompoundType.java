package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Arrays;
import java.util.List;

/**
 * Real type for types that are made up of a collection of other types. For example, a GPS position is made up of two
 * double sub types, one for latitude, one for longitude
 * @param <O> the type of the type's value
 */
public abstract class RealCompoundType<O>
        extends RealTypeImpl<O>
        implements SubType.Container<RealListImpl<RealSubTypeImpl<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final RealListImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, RealSubTypeImpl<?>... subTypes) {
        this(logger, listenersFactory, id, name, description, minValues, maxValues, Arrays.asList(subTypes));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, List<RealSubTypeImpl<?>> subTypes) {
        super(logger, new CompoundData(id, name, description, minValues, maxValues), listenersFactory);
        this.subTypes = new RealListImpl<>(ChildUtil.logger(logger, SUB_TYPES_ID), new com.intuso.housemate.client.api.internal.object.List.Data(SUB_TYPES_ID, SUB_TYPES_NAME, SUB_TYPES_DESCRIPTION), listenersFactory, subTypes);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        subTypes.init(ChildUtil.name(name, SUB_TYPES_ID), session);
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
