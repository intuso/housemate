package com.intuso.housemate.comms.api.internal.access;

import com.intuso.housemate.comms.api.internal.Message;

/**
 * Message payload for an access request
 */
public final class ApplicationRegistration implements Message.Payload {

    private static final long serialVersionUID = -1L;

    public final static String APPLICATION_REGISTRATION_TYPE = "application-registration";
    public final static String APPLICATION_UNREGISTRATION_TYPE = "application-unregistration";

    private ApplicationDetails applicationDetails;
    private String applicationInstanceId;
    private String component;
    private ClientType type;

    public ApplicationRegistration() {}

    /**
     * @param type the type of the connection. NB this affects what data you can/can't receive. For user interface
     *             applications you should use {@link ClientType#Proxy}
     */
    public ApplicationRegistration(ApplicationDetails applicationDetails, String applicationInstanceId,
                                   String component, ClientType type) {
        this.applicationDetails = applicationDetails;
        this.applicationInstanceId = applicationInstanceId;
        this.component = component;
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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
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

    /**
     *
     * Enumeration of all the types of client connections
    */
    public static enum ClientType {
        Router,
        Real,
        Proxy
    }
}
