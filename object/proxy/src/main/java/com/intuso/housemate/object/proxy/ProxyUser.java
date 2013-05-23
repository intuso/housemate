package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.api.object.user.UserWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyUser<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            C extends ProxyCommand<?, ?, ?, ?, C>,
            U extends ProxyUser<R, SR, C, U>>
        extends ProxyObject<R, SR, UserWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, U, UserListener>
        implements User<C> {

    private C removeCommand;

    public ProxyUser(R resources, SR subResources, UserWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        removeCommand = (C) getWrapper(REMOVE_COMMAND);
    }

    @Override
    public C getRemoveCommand() {
        return removeCommand;
    }
}
