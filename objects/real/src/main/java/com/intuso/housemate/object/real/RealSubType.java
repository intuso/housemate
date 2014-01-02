package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.type.TypeData;

/**
 * @param <O> the type of the sub type's value
 */
public class RealSubType<O>
        extends RealObject<SubTypeData, NoChildrenData, RealObject<NoChildrenData, ? ,?, ?>, SubTypeListener>
        implements SubType<RealType<?, ?, O>> {

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    /**
     * @param resources {@inheritDoc}
     * @param id the sub type's id
     * @param name the sub type's name
     * @param description the sub type's description
     * @param types the types in the system
     */
    public RealSubType(RealResources resources, String id, String name, String description, String typeId,
                       RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(resources, new SubTypeData(id, name, description, typeId));
        this.types = types;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return (RealType<?, ?, O>) types.get(getData().getType());
    }
}
