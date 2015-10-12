package com.intuso.housemate.comms.api.bridge.v1_0;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;

/**
 * Created by tomc on 02/10/15.
 */
public class ApplicationDetailsMapper {

    public ApplicationDetails map(com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails applicationDetails) {
        if(applicationDetails == null)
            return null;
        return new ApplicationDetails(applicationDetails.getApplicationId(),
                applicationDetails.getApplicationName(),
                applicationDetails.getApplicationDescription());
    }

    public com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails map(ApplicationDetails applicationDetails) {
        if(applicationDetails == null)
            return null;
        return new com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails(applicationDetails.getApplicationId(),
                applicationDetails.getApplicationName(),
                applicationDetails.getApplicationDescription());
    }
}
