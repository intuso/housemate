package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyUser
        extends ServerProxyObject<
            UserData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyUser,
            UserListener>
        implements User<
            ServerProxyCommand,
            ServerProxyProperty> {

    private ServerProxyCommand remove;
    private ServerProxyProperty email;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyUser(Log log, ListenersFactory listenersFactory, Injector injector, BooleanType booleanType, @Assisted UserData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(REMOVE_ID);
        email = (ServerProxyProperty) getChild(EMAIL_ID);
    }

    @Override
    public ServerProxyProperty getEmailProperty() {
        return email;
    }

    @Override
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof UserData)
            getEmailProperty().copyValues(data.getChildData(EMAIL_ID));
    }
}
