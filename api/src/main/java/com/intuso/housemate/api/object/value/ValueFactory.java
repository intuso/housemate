package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for values
 */
public interface ValueFactory<
            R extends Resources,
            V extends Value<?, ?>>
        extends HousemateObjectFactory<R, ValueData, V> {
}
