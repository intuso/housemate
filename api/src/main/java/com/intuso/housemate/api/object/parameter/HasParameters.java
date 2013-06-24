package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of parameters
 */
public interface HasParameters<L extends List<? extends Parameter<?>>> {

    /**
     * Gets the parameters list
     * @return the parameters list
     */
    public L getParameters();
}
