package com.intuso.housemate.api.comms.access;

import com.intuso.housemate.api.comms.ClientType;
import com.intuso.housemate.api.comms.Message;

/**
 * Message payload for an access request
 */
public final class ApplicationRegistration implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private ApplicationDetails applicationDetails;
    private String applicationInstanceId;
    private ClientType type;

    private ApplicationRegistration() {}

    /**
     * @param type the type of the connection. NB this affects what data you can/can't receive. For user interface
     *             applications you should use {@link com.intuso.housemate.api.comms.ClientType#Proxy}
     */
    public ApplicationRegistration(ApplicationDetails applicationDetails, String applicationInstanceId,
                                   ClientType type) {
        this.applicationDetails = applicationDetails;
        this.applicationInstanceId = applicationInstanceId;
        this.type = type;
    }

    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public String getApplicationInstanceId() {
        return applicationInstanceId;
    }

    /**
     * Gets the connection type for the request
     * @return the connection type for the request
     */
    public ClientType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name();
    }
}
