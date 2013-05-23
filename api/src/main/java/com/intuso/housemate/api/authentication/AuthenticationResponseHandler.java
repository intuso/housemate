package com.intuso.housemate.api.authentication;

import com.intuso.housemate.api.object.root.Root;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/05/13
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */
public interface AuthenticationResponseHandler {
    public void responseReceived(Root.AuthenticationResponse response);
}
