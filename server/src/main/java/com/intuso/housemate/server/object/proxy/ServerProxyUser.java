package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.User;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerProxyUser
        extends ServerProxyObject<
        UserData,
        HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyUser,
            User.Listener<? super ServerProxyUser>>
        implements User<
            ServerProxyCommand,
            ServerProxyProperty,
            ServerProxyUser> {

    private ServerProxyCommand remove;
    private ServerProxyProperty email;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyUser(ListenersFactory listenersFactory,
                           ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                           @Assisted Logger logger,
                           @Assisted UserData data) {
        super(listenersFactory, objectFactory, logger, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(UserData.REMOVE_ID);
        email = (ServerProxyProperty) getChild(UserData.EMAIL_ID);
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
            getEmailProperty().copyValues(data.getChildData(UserData.EMAIL_ID));
    }
}
