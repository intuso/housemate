package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.type.Email;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.User;

public interface RealUser
        extends User<RealCommand, RealProperty<Email>, RealUser> {

    interface RemovedListener {
        void userRemoved(RealUser user);
    }

    interface Factory {
        RealUser create(UserData data, RemovedListener removedListener);
    }
}
