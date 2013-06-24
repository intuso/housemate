package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of sub types
 */
public interface HasSubTypes<L extends List<? extends SubType<?>>> {

    /**
     * Gets the sub type list
     * @return the sub type list
     */
    public L getSubTypes();
}
