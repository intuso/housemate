package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for sub types
 */
public interface SubTypeFactory<
            ST extends SubType<?>>
        extends HousemateObjectFactory<SubTypeData, ST> {
}
