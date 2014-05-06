package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for users
 */
public interface UserFactory<
            U extends User<?, ?>>
        extends HousemateObjectFactory<UserData, U> {
}
