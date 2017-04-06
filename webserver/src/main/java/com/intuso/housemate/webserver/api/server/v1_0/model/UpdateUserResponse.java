package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 08/02/17.
 */
public class UpdateUserResponse {

    private boolean validEmail;
    private boolean alreadyRegistered;
    private boolean validServerAddress;

    public UpdateUserResponse() {}

    public UpdateUserResponse(boolean validEmail, boolean alreadyRegistered, boolean validServerAddress) {
        this.validEmail = validEmail;
        this.alreadyRegistered = alreadyRegistered;
        this.validServerAddress = validServerAddress;
    }

    public boolean isValidEmail() {
        return validEmail;
    }

    public void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }

    public boolean isAlreadyRegistered() {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered(boolean alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public boolean isValidServerAddress() {
        return validServerAddress;
    }

    public void setValidServerAddress(boolean validServerAddress) {
        this.validServerAddress = validServerAddress;
    }
}
