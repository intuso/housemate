package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public final class UserWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private UserWrappable() {}

    public UserWrappable(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new UserWrappable(getId(), getName(), getDescription());
    }
}
