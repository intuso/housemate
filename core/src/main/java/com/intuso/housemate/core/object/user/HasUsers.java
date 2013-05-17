package com.intuso.housemate.core.object.user;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public interface HasUsers<L extends List<? extends User>> {
    public L getUsers();
}
