package com.intuso.housemate.object.api.bridge.v1_0;

import com.intuso.housemate.object.api.internal.ApplicationInstance;

/**
 * Created by tomc on 02/10/15.
 */
public class ApplicationInstanceStatusMapper {

    public ApplicationInstance.Status map(com.intuso.housemate.object.v1_0.api.ApplicationInstance.Status applicationInstanceStatus) {
        if(applicationInstanceStatus == null)
            return null;
        return ApplicationInstance.Status.valueOf(applicationInstanceStatus.name());
    }

    public com.intuso.housemate.object.v1_0.api.ApplicationInstance.Status map(ApplicationInstance.Status applicationInstanceStatus) {
        if(applicationInstanceStatus == null)
            return null;
        return com.intuso.housemate.object.v1_0.api.ApplicationInstance.Status.valueOf(applicationInstanceStatus.name());
    }
}
