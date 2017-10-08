package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.internal.ProxyRenameable;
import com.intuso.housemate.client.proxy.internal.object.view.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the command
 * @param <USER> the type of the user
 */
public abstract class ProxyUser<
        COMMAND extends ProxyCommand<?, ?, ?>,
        PROPERTY extends ProxyProperty<?, ?, ?>,
        USER extends ProxyUser<COMMAND, PROPERTY, USER>>
        extends ProxyObject<User.Data, User.Listener<? super USER>, UserView>
        implements User<COMMAND, COMMAND, PROPERTY, USER>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<PROPERTY> propertyFactory;

    private COMMAND renameCommand;
    private COMMAND removeCommand;
    private PROPERTY emailProperty;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyUser(Logger logger,
                     String name,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory,
                     ProxyObject.Factory<COMMAND> commandFactory,
                     ProxyObject.Factory<PROPERTY> propertyFactory) {
        super(logger, name, User.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.propertyFactory = propertyFactory;
    }

    @Override
    public UserView createView() {
        return new UserView();
    }

    @Override
    public void view(UserView view) {

        super.view(view);

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(emailProperty == null)
                    emailProperty = propertyFactory.create(ChildUtil.logger(logger, EMAIL_ID), ChildUtil.name(name, EMAIL_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommandView() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommandView() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(emailProperty == null && view.getEmailPropertyView() != null)
                    emailProperty = propertyFactory.create(ChildUtil.logger(logger, EMAIL_ID), ChildUtil.name(name, EMAIL_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.view(new CommandView(View.Mode.ANCESTORS));
                removeCommand.view(new CommandView(View.Mode.ANCESTORS));
                emailProperty.view(new PropertyView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommandView() != null)
                    renameCommand.view(view.getRenameCommandView());
                if(view.getRemoveCommandView() != null)
                    removeCommand.view(view.getRemoveCommandView());
                if(view.getEmailPropertyView() != null)
                    emailProperty.view(view.getEmailPropertyView());
                break;
        }
    }

    @Override
    public void viewRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.view(commandView);
    }

    @Override
    public void viewRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.view(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(removeCommand != null)
            removeCommand.uninit();
        if(emailProperty != null)
            emailProperty.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public PROPERTY getEmailProperty() {
        return emailProperty;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        } else if(EMAIL_ID.equals(id)) {
            if(emailProperty == null)
                emailProperty = propertyFactory.create(ChildUtil.logger(logger, EMAIL_ID), ChildUtil.name(name, EMAIL_ID));
            return emailProperty;
        }
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:21
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyUser<
            ProxyCommand.Simple,
            ProxyProperty.Simple,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyProperty.Simple> propertyFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, propertyFactory);
        }
    }
}
