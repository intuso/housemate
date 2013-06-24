package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of options
 */
public interface HasOptions<L extends List<? extends Option<?>>> {

    /**
     * Gets the option list
     * @return the option list
     */
    public L getOptions();
}
