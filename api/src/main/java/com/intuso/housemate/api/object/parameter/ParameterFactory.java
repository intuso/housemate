package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for parameters
 */
public interface ParameterFactory<
            R extends Resources,
            P extends Parameter<?>>
        extends HousemateObjectFactory<R, ParameterData, P> {
}
