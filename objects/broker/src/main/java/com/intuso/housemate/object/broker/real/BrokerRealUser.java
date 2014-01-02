package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.broker.LifecycleHandler;

public class BrokerRealUser
        extends BrokerRealObject<UserData, HousemateData<?>, BrokerRealObject<?, ? ,?, ?>, UserListener>
        implements User<BrokerRealCommand> {

    private final BrokerRealCommand remove;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public BrokerRealUser(BrokerRealResources resources, String id, String name, String description,
                          LifecycleHandler lifecycleHandler) {
        super(resources, new UserData(id, name, description));
        this.remove = lifecycleHandler.createRemoveUserCommand(this);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }
}
