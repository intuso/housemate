package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;

/**
 * @param <O> the type of the sub type's value
 */
public class RealSubType<O>
        extends RealObject<SubTypeData, NoChildrenData, RealObject<NoChildrenData, ? ,?, ?>, SubTypeListener>
        implements SubType<RealType<?, ?, O>> {

    private final RealType<?, ?, O> type;

    /**
     * @param resources {@inheritDoc}
     * @param id the sub type's id
     * @param name the sub type's name
     * @param description the sub type's description
     * @param type the type of the sub type's value
     */
    public RealSubType(RealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new SubTypeData(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
