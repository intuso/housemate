package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.User;

public interface RealUser<COMMAND extends RealCommand<?, ?, ?>,
        EMAIL_PROPERTY extends RealProperty<String, ?, ?, ?>,
        USER extends RealUser<COMMAND, EMAIL_PROPERTY, USER>>
        extends User<COMMAND,
        COMMAND,
        EMAIL_PROPERTY,
        USER> {}
