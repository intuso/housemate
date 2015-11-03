package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Application;

public interface RealApplication
        extends Application<
            RealValue<Application.Status>,
            RealCommand,
            RealApplicationInstance,
        com.intuso.housemate.client.real.api.internal.RealList<? extends RealApplicationInstance>,
        RealApplication> {

    void setStatus(Status status);
}
