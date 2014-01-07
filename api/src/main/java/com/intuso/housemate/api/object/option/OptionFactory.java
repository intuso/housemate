package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for options
 */
public interface OptionFactory<
            O extends Option<?>>
        extends HousemateObjectFactory<OptionData, O> {
}
