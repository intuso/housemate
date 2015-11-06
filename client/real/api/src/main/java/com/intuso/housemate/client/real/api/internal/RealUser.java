package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.type.Email;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.User;

public interface RealUser
        extends User<RealCommand, RealProperty<Email>, RealUser> {

    interface Container extends User.Container<RealList<RealUser>>, RemoveCallback {
        void addUser(RealUser user);
    }

    interface RemoveCallback {
        void removeUser(RealUser user);
    }

    interface Factory {
        RealUser create(UserData data, RemoveCallback removeCallback);
    }
}
