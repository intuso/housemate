package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.ApplicationInstance;

public interface RealApplicationInstance
        extends ApplicationInstance<com.intuso.housemate.client.real.api.internal.RealValue<ApplicationInstance.Status>, RealCommand, RealApplicationInstance> {

    interface Container extends ApplicationInstance.Container<RealList<RealApplicationInstance>> {
        void addApplicationInstance(RealApplicationInstance applicationInstance);
        void removeApplicationInstance(RealApplicationInstance applicationInstance);
    }
}
