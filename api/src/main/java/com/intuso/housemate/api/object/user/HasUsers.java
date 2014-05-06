package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of users
 */
public interface HasUsers<L extends List<? extends User<?, ?>>> {

    /**
     * Gets the user list
     * @return the user list
     */
    public L getUsers();
}
