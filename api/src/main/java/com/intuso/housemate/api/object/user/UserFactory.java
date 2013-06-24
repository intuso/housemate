package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for users
 */
public interface UserFactory<
            R extends Resources,
            U extends User<?>>
        extends HousemateObjectFactory<R, UserWrappable, U> {
}
