package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for properties
 */
public interface PropertyFactory<
            R extends Resources,
            P extends Property<?, ?, ?>>
        extends HousemateObjectFactory<R, PropertyWrappable, P> {
}
