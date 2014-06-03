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

    public ApplicationRegistration() {}

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

    public void setApplicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
    }

    public String getApplicationInstanceId() {
        return applicationInstanceId;
    }

    public void setApplicationInstanceId(String applicationInstanceId) {
        this.applicationInstanceId = applicationInstanceId;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }

    @Override
    public void ensureSerialisable() {}
}
