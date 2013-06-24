package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for options
 */
public interface OptionFactory<
            R extends Resources,
            O extends Option<?>>
        extends HousemateObjectFactory<R, OptionWrappable, O> {
}
