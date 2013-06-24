package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for housemate objects
 */
public interface HousemateObjectFactory<
            R extends Resources,
            WBL extends HousemateObjectWrappable<?>,
            O extends BaseObject<?>> {
    /**
     * Create a new instance of an object
     * @param resources the resources
     * @param wrappable the object's data
     * @return a new object
     * @throws HousemateException
     */
    public O create(R resources, WBL wrappable) throws HousemateException;
}
