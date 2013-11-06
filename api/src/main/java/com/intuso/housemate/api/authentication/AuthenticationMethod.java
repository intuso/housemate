package com.intuso.housemate.api.authentication;

import java.io.Serializable;

/**
* Created with IntelliJ IDEA.
* User: tom
* Date: 02/05/13
* Time: 18:25
 *
 * Base class for all authentication methods
 *
 * @see com.intuso.housemate.api.object.root.Root#login(AuthenticationMethod)
*/
public interface AuthenticationMethod extends Serializable {

    /**
     * Is this authentication method allowing clients to use it
     * @return true if this authentication method allows clients to use it
     */
    public boolean isClientsAuthenticated();
}
