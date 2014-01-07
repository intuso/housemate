package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for parameters
 */
public interface ParameterFactory<
            P extends Parameter<?>>
        extends HousemateObjectFactory<ParameterData, P> {
}
