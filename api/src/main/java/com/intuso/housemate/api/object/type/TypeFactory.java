package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for types
 */
public interface TypeFactory<
            R extends Resources,
            T extends Type>
        extends HousemateObjectFactory<R, TypeWrappable<?>, T> {
}
