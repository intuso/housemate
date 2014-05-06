package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <COMMAND> the type of the command
 * @param <USER> the type of the user
 */
public abstract class ProxyUser<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            USER extends ProxyUser<COMMAND, PROPERTY, USER>>
        extends ProxyObject<UserData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, USER, UserListener>
        implements User<COMMAND, PROPERTY> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyUser(Log log, ListenersFactory listenersFactory, UserData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(REMOVE_ID);
    }

    @Override
    public PROPERTY getEmailProperty() {
        return (PROPERTY) getChild(EMAIL_ID);
    }
}
