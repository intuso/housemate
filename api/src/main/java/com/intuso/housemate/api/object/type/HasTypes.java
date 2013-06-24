package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of types
 */
public interface HasTypes<L extends List<? extends Type>> {

    /**
     * Gets the type list
     * @return the type list
     */
    public L getTypes();
}
