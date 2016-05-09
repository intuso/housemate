package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.real.api.internal.type.Email;

public interface RealUser<COMMAND extends RealCommand<?, ?, ?>,
        EMAIL_PROPERTY extends RealProperty<Email, ?, ?, ?>,
        USER extends RealUser<COMMAND, EMAIL_PROPERTY, USER>>
        extends User<COMMAND,
                COMMAND,
                EMAIL_PROPERTY,
                USER> {

    interface Container<USER extends RealUser<?, ?, ?>, USERS extends com.intuso.housemate.client.real.api.internal.RealList<? extends USER, ?>> extends User.Container<USERS>, RemoveCallback<USER> {
        void addUser(USER user);
    }

    interface RemoveCallback<USER extends RealUser<?, ?, ?>> {
        void removeUser(USER user);
    }
}
