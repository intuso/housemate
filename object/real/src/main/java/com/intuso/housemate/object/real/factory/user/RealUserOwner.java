package com.intuso.housemate.object.real.factory.user;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealUser;

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
