package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.user.User;
import com.intuso.housemate.core.object.user.UserListener;
import com.intuso.housemate.core.object.user.UserWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class UserBridge
        extends BridgeObject<UserWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, UserListener>
        implements User<CommandBridge> {

    private final CommandBridge removeCommand;

    public UserBridge(BrokerBridgeResources resources, User user) {
        super(resources, new UserWrappable(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(resources, user.getRemoveCommand());
        addWrapper(removeCommand);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }
}
