package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.api.object.user.UserWrappable;

public class BrokerRealUser
        extends BrokerRealObject<UserWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ? ,?, ?>, UserListener>
        implements User<BrokerRealCommand> {

    private final BrokerRealCommand remove;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public BrokerRealUser(BrokerRealResources resources, String id, String name, String description) {
        super(resources, new UserWrappable(id, name, description));
        this.remove = getResources().getLifecycleHandler().createRemoveUserCommand(this);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }
}
