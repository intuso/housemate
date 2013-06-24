package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 */
public abstract class RealType<WBL extends TypeWrappable<SWBL>, SWBL extends HousemateObjectWrappable<?>, O>
        extends RealObject<WBL, SWBL, RealObject<SWBL, ?, ?, ?>, TypeListener>
        implements Type, TypeSerialiser<O> {

    protected RealType(RealResources resources, WBL wrappable) {
        super(resources, wrappable);
    }
}
