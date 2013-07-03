package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.subtype.HasSubTypes;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.CompoundTypeWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.List;

/**
 * Real type for types that are made up of a collection of other types. For example, a GPS position is made up of two
 * double sub types, one for latitude, one for longitude
 * @param <O> the type of the type's value
 */
public abstract class RealCompoundType<O>
        extends RealType<CompoundTypeWrappable, ListWrappable<SubTypeWrappable>, O>
        implements HasSubTypes<RealList<SubTypeWrappable, RealSubType<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final RealList<SubTypeWrappable, RealSubType<?>> subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(RealResources resources, String id, String name, String description, int minValues,
                               int maxValues, RealSubType<?> ... subTypes) {
        this(resources, id, name, description, minValues, maxValues, Arrays.asList(subTypes));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(RealResources resources, String id, String name, String description, int minValues,
                               int maxValues, List<RealSubType<?>> subTypes) {
        super(resources, new CompoundTypeWrappable(id, name, description, minValues, maxValues));
        this.subTypes = new RealList<SubTypeWrappable, RealSubType<?>>(resources, SUB_TYPES_ID, SUB_TYPES_NAME, SUB_TYPES_DESCRIPTION);
        addWrapper(this.subTypes);
        for(RealSubType<?> subType : subTypes)
            this.subTypes.add(subType);
    }

    @Override
    public final RealList<SubTypeWrappable, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
