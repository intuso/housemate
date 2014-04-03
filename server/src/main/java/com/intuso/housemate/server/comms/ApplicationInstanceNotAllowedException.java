package com.intuso.housemate.server.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.object.server.RemoteClient;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/02/14
 * Time: 09:06
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceNotAllowedException extends HousemateException {
    public ApplicationInstanceNotAllowedException(RemoteClient client) {
        super("Application instance " + client.getClientInstance() + " sent a message but it does not have status " + ApplicationInstanceStatus.Allowed);
    }
}
