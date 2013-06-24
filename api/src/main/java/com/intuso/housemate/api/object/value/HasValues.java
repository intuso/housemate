package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of values
 */
public interface HasValues<L extends List<? extends Value<?, ?>>> {

    /**
     * Gets the value list
     * @return the value list
     */
    public L getValues();
}
