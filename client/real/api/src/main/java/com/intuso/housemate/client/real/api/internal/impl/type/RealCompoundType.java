package com.intuso.housemate.client.real.api.internal.impl.type;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealSubType;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.comms.api.internal.payload.CompoundTypeData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Real type for types that are made up of a collection of other types. For example, a GPS position is made up of two
 * double sub types, one for latitude, one for longitude
 * @param <O> the type of the type's value
 */
public abstract class RealCompoundType<O>
        extends RealType<CompoundTypeData, ListData<SubTypeData>, O>
        implements SubType.Container<RealList<SubTypeData, RealSubType<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final RealList<SubTypeData, RealSubType<?>> subTypes;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, RealSubType<?>... subTypes) {
        this(log, listenersFactory, id, name, description, minValues, maxValues, Arrays.asList(subTypes));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, List<RealSubType<?>> subTypes) {
        super(log, listenersFactory, new CompoundTypeData(id, name, description, minValues, maxValues));
        this.subTypes = new RealList<>(log, listenersFactory, SUB_TYPES_ID, SUB_TYPES_NAME, SUB_TYPES_DESCRIPTION);
        addChild(this.subTypes);
        for(RealSubType<?> subType : subTypes)
            this.subTypes.add(subType);
    }

    @Override
    public final RealList<SubTypeData, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
