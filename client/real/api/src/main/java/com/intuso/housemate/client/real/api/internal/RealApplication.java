package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Application;

public interface RealApplication
        extends Application<
        RealValue<Application.Status>,
        RealCommand,
        RealApplicationInstance,
        RealList<RealApplicationInstance>,
        RealApplication>,
        RealApplicationInstance.Container {

    void setStatus(Status status);

    interface Container extends Application.Container<RealList<RealApplication>> {
        void addApplication(RealApplication application);
        void removeApplication(RealApplication application);
    }
}
