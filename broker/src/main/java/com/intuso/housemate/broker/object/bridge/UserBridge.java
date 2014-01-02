package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;

/**
 */
public class UserBridge
        extends BridgeObject<UserData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, UserListener>
        implements User<CommandBridge> {

    private final CommandBridge removeCommand;

    public UserBridge(BrokerBridgeResources resources, User user,
                      ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
        super(resources, new UserData(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(resources, user.getRemoveCommand(), types);
        addChild(removeCommand);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    public final static class Converter implements Function<User<?>, UserBridge> {

        private final BrokerBridgeResources resources;
        private final ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types;

        public Converter(BrokerBridgeResources resources, ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
            this.resources = resources;
            this.types = types;
        }

        @Override
        public UserBridge apply(User<?> user) {
            return new UserBridge(resources, user, types);
        }
    }
}
