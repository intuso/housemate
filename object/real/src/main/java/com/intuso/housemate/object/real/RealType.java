package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the children's data object
 * @param <O> the type of the type instances
 */
public abstract class RealType<
            DATA extends TypeWrappable<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            O>
        extends RealObject<DATA, CHILD_DATA, RealObject<CHILD_DATA, ?, ?, ?>, TypeListener>
        implements Type, TypeSerialiser<O> {

    /**
     * @param resources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    protected RealType(RealResources resources, DATA wrappable) {
        super(resources, wrappable);
    }
}
