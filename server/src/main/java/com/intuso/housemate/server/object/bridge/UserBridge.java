package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class UserBridge
        extends BridgeObject<UserData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, UserListener>
        implements User<CommandBridge, PropertyBridge> {

    private final CommandBridge removeCommand;
    private final PropertyBridge emailProperty;

    public UserBridge(Log log, ListenersFactory listenersFactory, User user,
                      ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new UserData(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, user.getRemoveCommand(), types);
        emailProperty = new PropertyBridge(log, listenersFactory, user.getEmailProperty(), types);
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

    public final static class Converter implements Function<User<?, ?>, UserBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public UserBridge apply(User<?, ?> user) {
            return new UserBridge(log, listenersFactory, user, types);
        }
    }
}
