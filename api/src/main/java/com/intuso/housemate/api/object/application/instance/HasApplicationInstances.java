package com.intuso.housemate.api.object.application.instance;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of application instances
 */
public interface HasApplicationInstances<L extends List<? extends ApplicationInstance<?, ?, ?>>> {

    /**
     * Gets the application instances list
     * @return the application instances list
     */
    public L getApplicationInstances();
}
