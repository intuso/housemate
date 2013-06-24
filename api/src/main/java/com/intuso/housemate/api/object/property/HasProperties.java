package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of properties
 */
public interface HasProperties<L extends List<? extends Property<?, ?, ?>>> {

    /**
     * Gets the properties list
     * @return the properties list
     */
    public L getProperties();
}
