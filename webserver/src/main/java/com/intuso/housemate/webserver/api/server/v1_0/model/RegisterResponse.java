package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 08/02/17.
 */
public class RegisterResponse {

    private boolean validEmail;
    private boolean alreadyRegistered;
    private boolean validPassword;
    private boolean success;

    public RegisterResponse() {}

    public RegisterResponse(boolean validEmail, boolean alreadyRegistered, boolean validPassword, boolean success) {
        this.validEmail = validEmail;
        this.alreadyRegistered = alreadyRegistered;
        this.validPassword = validPassword;
        this.success = success;
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

    public boolean isValidPassword() {
        return validPassword;
    }

    public void setValidPassword(boolean validPassword) {
        this.validPassword = validPassword;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
