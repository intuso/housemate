package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.UserMapper;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

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
                              Factory<ProxyPropertyBridge> propertyFactory,
                              ManagedCollectionFactory managedCollectionFactory,
                              com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                              Sender.Factory v1_0SenderFactory) {
        super(logger, User.Data.class, userMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        emailProperty = propertyFactory.create(ChildUtil.logger(logger, User.EMAIL_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID)
        );
        removeCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID)
        );
        emailProperty.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.User.EMAIL_ID),
                ChildUtil.name(internalName, User.EMAIL_ID)
        );
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
