package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealSimpleType<O> extends RealType<SimpleTypeWrappable, NoChildrenWrappable, O> {
    protected RealSimpleType(RealResources resources, SimpleTypeWrappable.Type type, TypeSerialiser<O> serialiser) {
        super(resources, new SimpleTypeWrappable(type), serialiser);
    }
}
