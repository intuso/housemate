package com.intuso.housemate.client.real.api.internal.factory.user;

import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.comms.api.internal.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealUserOwner {
    public ChildOverview getAddUserCommandDetails();
    public void addUser(RealUser user);
    public void removeUser(RealUser user);
}
