package com.intuso.housemate.client.real.api.internal.factory.user;

import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.comms.api.internal.payload.UserData;

/**
* Created by tomc on 20/03/15.
*/
public interface RealUserFactory {
    public RealUser create(UserData data, RealUserOwner owner);
}
