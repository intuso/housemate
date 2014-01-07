package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for properties
 */
public interface PropertyFactory<
            P extends Property<?, ?, ?>>
        extends HousemateObjectFactory<PropertyData, P> {
}
