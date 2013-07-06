package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for sub types
 */
public interface SubTypeFactory<
            R extends Resources,
            ST extends SubType<?>>
        extends HousemateObjectFactory<R, SubTypeData, ST> {
}
