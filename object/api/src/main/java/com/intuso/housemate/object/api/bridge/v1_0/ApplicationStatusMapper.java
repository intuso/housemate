package com.intuso.housemate.object.api.bridge.v1_0;

import com.intuso.housemate.object.api.internal.Application;

/**
 * Created by tomc on 02/10/15.
 */
public class ApplicationStatusMapper {

    public Application.Status map(com.intuso.housemate.object.v1_0.api.Application.Status applicationStatus) {
        if(applicationStatus == null)
            return null;
        return Application.Status.valueOf(applicationStatus.name());
    }

    public com.intuso.housemate.object.v1_0.api.Application.Status map(Application.Status applicationStatus) {
        if(applicationStatus == null)
            return null;
        return com.intuso.housemate.object.v1_0.api.Application.Status.valueOf(applicationStatus.name());
    }
}
