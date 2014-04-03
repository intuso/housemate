package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerRealUser
        extends ServerRealObject<UserData, HousemateData<?>, ServerRealObject<?, ? ,?, ?>, UserListener>
        implements User<ServerRealCommand> {

    private final ServerRealCommand remove;

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public ServerRealUser(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                          final ServerRealUserOwner owner) {
        super(log, listenersFactory, new UserData(id, name, description));
        this.remove = new ServerRealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the user", Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.remove(ServerRealUser.this);
            }
        };
    }

    @Override
    public ServerRealCommand getRemoveCommand() {
        return remove;
    }
}
