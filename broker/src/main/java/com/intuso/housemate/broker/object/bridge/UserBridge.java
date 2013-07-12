package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;

import javax.annotation.Nullable;

/**
 */
public class UserBridge
        extends BridgeObject<UserData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, UserListener>
        implements User<CommandBridge> {

    private final CommandBridge removeCommand;

    public UserBridge(BrokerBridgeResources resources, User user) {
        super(resources, new UserData(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(resources, user.getRemoveCommand());
        addChild(removeCommand);
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
