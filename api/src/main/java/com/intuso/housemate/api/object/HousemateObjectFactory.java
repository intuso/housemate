package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;

/**
 *
 * Factory for housemate objects
 */
public interface HousemateObjectFactory<
            WBL extends HousemateData<?>,
            O extends BaseHousemateObject<?>> {
    /**
     * Create a new instance of an object
     * @param data the object's data
     * @return a new object
     * @throws HousemateException
     */
    public O create(WBL data);
}
