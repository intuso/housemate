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
 */
public abstract class RealCompoundType<O>
        extends RealType<CompoundTypeWrappable, ListWrappable<SubTypeWrappable>, O>
        implements HasSubTypes<RealList<SubTypeWrappable, RealSubType<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final RealList<SubTypeWrappable, RealSubType<?>> subTypes;

    protected RealCompoundType(RealResources resources, String id, String name, String description,
                               RealSubType<?> ... subTypes) {
        this(resources, id, name, description, Arrays.asList(subTypes));
    }

    protected RealCompoundType(RealResources resources, String id, String name, String description,
                               List<RealSubType<?>> subTypes) {
        super(resources, new CompoundTypeWrappable(id, name, description));
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
