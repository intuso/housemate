package com.intuso.housemate.server.comms;

import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.object.api.internal.ApplicationInstance;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/02/14
 * Time: 09:06
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceNotAllowedException extends HousemateCommsException {
    public ApplicationInstanceNotAllowedException(RemoteClient client) {
        super("Application instance " + client.getClientInstance() + " sent a message but it does not have status " + ApplicationInstance.Status.Allowed);
    }
}
