package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.UserMapper;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyUserBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.User.Data, User.Data, User.Listener<? super ProxyUserBridge>>
        implements User<ProxyCommandBridge, ProxyCommandBridge, ProxyPropertyBridge, ProxyUserBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyPropertyBridge emailProperty;

    @Inject
    protected ProxyUserBridge(@Assisted Logger logger,
                              UserMapper userMapper,
                              Factory<ProxyCommandBridge> commandFactory,
                              Factory<ProxyValueBridge> valueFactory,
                              Factory<ProxyPropertyBridge> propertyFactory,
                              Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                              ListenersFactory listenersFactory) {
        super(logger, User.Data.class, userMapper, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        emailProperty = propertyFactory.create(ChildUtil.logger(logger, User.EMAIL_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        emailProperty.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.User.EMAIL_ID),
                ChildUtil.name(internalName, User.EMAIL_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        emailProperty.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ProxyPropertyBridge getEmailProperty() {
        return emailProperty;
    }
}
