package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.api.object.user.UserWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the chil resources
 * @param <COMMAND> the type of the command
 * @param <USER> the type of the user
 */
public abstract class ProxyUser<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            USER extends ProxyUser<RESOURCES, CHILD_RESOURCES, COMMAND, USER>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, UserWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, USER, UserListener>
        implements User<COMMAND> {

    private COMMAND removeCommand;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyUser(RESOURCES resources, CHILD_RESOURCES childResources, UserWrappable data) {
        super(resources, childResources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        removeCommand = (COMMAND) getWrapper(REMOVE_COMMAND_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }
}
