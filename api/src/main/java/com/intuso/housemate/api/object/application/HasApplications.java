package com.intuso.housemate.api.object.application;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of applications
 */
public interface HasApplications<L extends List<? extends Application<?, ?, ?, ?, ?>>> {

    /**
     * Gets the application list
     * @return the application list
     */
    public L getApplications();
}
