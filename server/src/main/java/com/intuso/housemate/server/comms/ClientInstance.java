package com.intuso.housemate.server.comms;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 27/01/14
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class ClientInstance {

    private final boolean internal;
    private final ApplicationDetails applicationDetails;
    private final String applicationInstanceId;
    private final String component;
    private final ApplicationRegistration.ClientType clientType;
    private final int hashCode;

    public ClientInstance(boolean internal, ApplicationDetails applicationDetails, String applicationInstanceId,
                          String component, ApplicationRegistration.ClientType clientType) {
        this.internal = internal;
        this.applicationDetails = applicationDetails;
        this.applicationInstanceId = applicationInstanceId;
        this.component = component;
        this.clientType = clientType;
        hashCode = (applicationDetails.getApplicationId() + "/" + applicationInstanceId + "/" + component + "/" + clientType).hashCode();
    }

    public boolean isInternal() {
        return internal;
    }

    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public String getApplicationInstanceId() {
        return applicationInstanceId;
    }

    public ApplicationRegistration.ClientType getClientType() {
        return clientType;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof ClientInstance))
            return false;
        ClientInstance other = (ClientInstance)o;
        boolean instanceIdsEqual = (other.applicationInstanceId == null && applicationInstanceId == null)
                || (other.applicationInstanceId != null && applicationInstanceId != null && other.applicationInstanceId.equals(applicationInstanceId));
        return other.applicationDetails.getApplicationId().equals(applicationDetails.getApplicationId())
                && instanceIdsEqual
                && other.clientType == clientType;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return applicationDetails.getApplicationId() + "/" + applicationInstanceId + "/" + clientType;
    }
}
