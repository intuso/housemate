package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.User;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class UserBridge
        extends BridgeObject<UserData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, User.Listener<? super UserBridge>>
        implements User<CommandBridge, PropertyBridge, UserBridge> {

    private final CommandBridge removeCommand;
    private final PropertyBridge emailProperty;

    public UserBridge(Log log, ListenersFactory listenersFactory, User user) {
        super(log, listenersFactory, new UserData(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, user.getRemoveCommand());
        emailProperty = new PropertyBridge(log, listenersFactory, user.getEmailProperty());
        addChild(removeCommand);
        addChild(emailProperty);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public PropertyBridge getEmailProperty() {
        return emailProperty;
    }

    public final static class Converter implements Function<User<?, ?, ?>, UserBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public UserBridge apply(User<?, ?, ?> user) {
            return new UserBridge(log, listenersFactory, user);
        }
    }
}
