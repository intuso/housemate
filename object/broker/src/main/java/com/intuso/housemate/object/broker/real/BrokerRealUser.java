package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.api.object.user.UserWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealUser
        extends BrokerRealObject<UserWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ? ,?, ?>, UserListener>
        implements User<BrokerRealCommand> {

    private final BrokerRealCommand remove;

    public BrokerRealUser(BrokerRealResources resources, String id, String name, String description) {
        super(resources, new UserWrappable(id, name, description));
        this.remove = getResources().getLifecycleHandler().createRemoveUserCommand(this);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }
}
