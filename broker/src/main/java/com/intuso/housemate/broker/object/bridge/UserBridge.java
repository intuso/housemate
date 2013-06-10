package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.api.object.user.UserWrappable;

import javax.annotation.Nullable;

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

    public final static class Converter implements Function<User<?>, UserBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public UserBridge apply(@Nullable User<?> user) {
            return new UserBridge(resources, user);
        }
    }
}
