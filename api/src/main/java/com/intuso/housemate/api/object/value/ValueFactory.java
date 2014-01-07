package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for values
 */
public interface ValueFactory<
            V extends Value<?, ?>>
        extends HousemateObjectFactory<ValueData, V> {
}
