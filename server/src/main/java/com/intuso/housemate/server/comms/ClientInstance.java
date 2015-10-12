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
public abstract class ClientInstance {

    private final boolean internal;

    public ClientInstance(boolean internal) {
        this.internal = internal;
    }

    public boolean isInternal() {
        return internal;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    public static class Application extends ClientInstance {

        private final ApplicationDetails applicationDetails;
        private final String applicationInstanceId;
        private final ApplicationRegistration.ClientType clientType;
        private final int hashCode;

        public Application(boolean internal, ApplicationDetails applicationDetails, String applicationInstanceId, String component, ApplicationRegistration.ClientType clientType) {
            super(internal);
            this.applicationDetails = applicationDetails;
            this.applicationInstanceId = applicationInstanceId;
            this.clientType = clientType;
            this.hashCode = (applicationDetails.getApplicationId() + "/" + applicationInstanceId + "/" + component + "/" + clientType).hashCode();
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
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null || !(o instanceof Application))
                return false;
            Application other = (Application)o;
            boolean instanceIdsEqual = (other.applicationInstanceId == null && applicationInstanceId == null)
                    || (other.applicationInstanceId != null && applicationInstanceId != null && other.applicationInstanceId.equals(applicationInstanceId));
            return other.applicationDetails.getApplicationId().equals(applicationDetails.getApplicationId())
                    && instanceIdsEqual
                    && other.clientType == clientType;
        }
    }

    public static class Router extends ClientInstance {

        private final String routerId;
        private final int hashCode;

        public Router(boolean internal, String routerId) {
            super(internal);
            this.routerId = routerId;
            this.hashCode = routerId.hashCode();
        }

        public String getRouterId() {
            return routerId;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null || !(o instanceof Router))
                return false;
            Router other = (Router) o;
            return routerId.equals(other.routerId);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
