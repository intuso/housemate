package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.server.LifecycleHandler;

public class ServerRealUser
        extends ServerRealObject<UserData, HousemateData<?>, ServerRealObject<?, ? ,?, ?>, UserListener>
        implements User<ServerRealCommand> {

    private final ServerRealCommand remove;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public ServerRealUser(ServerRealResources resources, String id, String name, String description,
                          LifecycleHandler lifecycleHandler) {
        super(resources, new UserData(id, name, description));
        this.remove = lifecycleHandler.createRemoveUserCommand(this);
    }

    @Override
    public ServerRealCommand getRemoveCommand() {
        return remove;
    }
}
