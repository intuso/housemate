package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for a user
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
