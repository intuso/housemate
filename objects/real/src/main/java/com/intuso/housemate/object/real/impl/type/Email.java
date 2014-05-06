package com.intuso.housemate.object.real.impl.type;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 01/05/14
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public class Email {

    private final String email;

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email1 = (Email) o;

        if (email != null ? !email.equals(email1.email) : email1.email != null) return false;

        return true;
    }
}
