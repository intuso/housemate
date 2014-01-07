package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for types
 */
public interface TypeFactory<
            T extends Type>
        extends HousemateObjectFactory<TypeData<HousemateData<?>>, T> {
}
