package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.HousemateData;

/**
 *
 * Data object for a user
 */
public final class UserData extends HousemateData<HousemateData<?>> {

    private UserData() {}

    public UserData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new UserData(getId(), getName(), getDescription());
    }
}
