package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 08/02/17.
 */
public class LoginResponse {

    private boolean validEmail;
    private boolean knownEmail;
    private boolean validPassword;
    private boolean correctPassword;

    public LoginResponse() {}

    public LoginResponse(boolean validEmail, boolean knownEmail, boolean validPassword, boolean correctPassword) {
        this.validEmail = validEmail;
        this.knownEmail = knownEmail;
        this.validPassword = validPassword;
        this.correctPassword = correctPassword;
    }

    public boolean isValidEmail() {
        return validEmail;
    }

    public void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }

    public boolean isKnownEmail() {
        return knownEmail;
    }

    public void setKnownEmail(boolean knownEmail) {
        this.knownEmail = knownEmail;
    }

    public boolean isValidPassword() {
        return validPassword;
    }

    public void setValidPassword(boolean validPassword) {
        this.validPassword = validPassword;
    }

    public boolean isCorrectPassword() {
        return correctPassword;
    }

    public void setCorrectPassword(boolean correctPassword) {
        this.correctPassword = correctPassword;
    }
}
