package com.intuso.housemate.object.real.factory.user;

import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.RealUser;

/**
* Created by tomc on 20/03/15.
*/
public interface RealUserFactory {
    public RealUser create(UserData data, RealUserOwner owner);
}
