package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of conditions
 */
public interface HasConditions<L extends List<? extends Condition<?, ?, ?, ?, ?, ?>>> {

    /**
     * Gets the conditions list
     * @return the conditions list
     */
    public L getConditions();
}
