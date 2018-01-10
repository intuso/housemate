package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.PropertyView;
import com.intuso.housemate.client.api.internal.object.view.UserView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

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
    public UserView createView(View.Mode mode) {
        return new UserView(mode);
    }

    @Override
    public Tree getTree(UserView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(EMAIL_ID, emailProperty.getTree(new PropertyView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener, listenerRegistrations));
                    result.getChildren().put(EMAIL_ID, emailProperty.getTree(view.getEmailProperty(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener, listenerRegistrations));
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener, listenerRegistrations));
                    if(view.getEmailProperty() != null)
                        result.getChildren().put(EMAIL_ID, emailProperty.getTree(view.getEmailProperty(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(UserView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

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
                if(renameCommand == null && view.getRenameCommand() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommand() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(emailProperty == null && view.getEmailProperty() != null)
                    emailProperty = propertyFactory.create(ChildUtil.logger(logger, EMAIL_ID), ChildUtil.name(name, EMAIL_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                removeCommand.load(new CommandView(View.Mode.ANCESTORS));
                emailProperty.load(new PropertyView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommand() != null)
                    renameCommand.load(view.getRenameCommand());
                if(view.getRemoveCommand() != null)
                    removeCommand.load(view.getRemoveCommand());
                if(view.getEmailProperty() != null)
                    emailProperty.load(view.getEmailProperty());
                break;
        }
    }

    @Override
    public void loadRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.load(commandView);
    }

    @Override
    public void loadRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.load(commandView);
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
