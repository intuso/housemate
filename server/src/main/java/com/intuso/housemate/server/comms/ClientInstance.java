package com.intuso.housemate.server.comms;

import com.intuso.housemate.api.comms.ClientType;
import com.intuso.housemate.api.comms.access.ApplicationDetails;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 27/01/14
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class ClientInstance {

    private final ApplicationDetails applicationDetails;
    private final String applicationInstanceId;
    private final ClientType clientType;
    private final int hashCode;

    public ClientInstance(ApplicationDetails applicationDetails, String applicationInstanceId,
                          ClientType clientType) {
        this.applicationDetails = applicationDetails;
        this.applicationInstanceId = applicationInstanceId;
        this.clientType = clientType;
        hashCode = (applicationDetails.getApplicationId() + "/" + applicationInstanceId + "/" + clientType).hashCode();
    }

    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public String getApplicationInstanceId() {
        return applicationInstanceId;
    }

    public ClientType getClientType() {
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
