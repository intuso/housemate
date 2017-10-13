package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.PropertyView;
import com.intuso.housemate.client.api.internal.object.view.UserView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base class for all user
 */
public final class RealUserImpl
        extends RealObject<User.Data, User.Listener<? super RealUserImpl>, UserView>
        implements RealUser<RealCommandImpl, RealPropertyImpl<String>, RealUserImpl> {

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealPropertyImpl<String> emailProperty;

    private final RealListPersistedImpl.RemoveCallback<RealUserImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealUserImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        @Assisted RealListPersistedImpl.RemoveCallback<RealUserImpl> removeCallback,
                        ManagedCollectionFactory managedCollectionFactory,
                        Sender.Factory senderFactory,
                        RealCommandImpl.Factory commandFactory,
                        RealParameterImpl.Factory parameterFactory,
                        RealPropertyImpl.Factory propertyFactory,
                        TypeRepository typeRepository) {
        super(logger, new User.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the user",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealUserImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the user",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.emailProperty = (RealPropertyImpl<String>) propertyFactory.create(ChildUtil.logger(logger, User.EMAIL_ID),
                User.EMAIL_ID,
                User.EMAIL_ID,
                User.EMAIL_ID,
                typeRepository.getType(new TypeSpec(String.class, "email")),
                1,
                1,
                Lists.<String>newArrayList());
    }

    @Override
    public UserView createView(View.Mode mode) {
        return new UserView(mode);
    }

    @Override
    public Tree getTree(UserView view) {

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(EMAIL_ID, emailProperty.getTree(new PropertyView(View.Mode.ANCESTORS)));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommandView()));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommandView()));
                    result.getChildren().put(EMAIL_ID, emailProperty.getTree(view.getEmailPropertyView()));
                    break;

                case SELECTION:
                    if(view.getRemoveCommandView() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommandView()));
                    if(view.getRenameCommandView() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommandView()));
                    if(view.getEmailPropertyView() != null)
                        result.getChildren().put(EMAIL_ID, emailProperty.getTree(view.getEmailPropertyView()));
                    break;
            }

        }

        return result;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID));
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID));
        emailProperty.init(ChildUtil.name(name, User.EMAIL_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        emailProperty.uninit();
    }

    private void setName(String newName) {
        RealUserImpl.this.getData().setName(newName);
        for(User.Listener<? super RealUserImpl> listener : listeners)
            listener.renamed(RealUserImpl.this, RealUserImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealPropertyImpl<String> getEmailProperty() {
        return emailProperty;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(EMAIL_ID.equals(id))
            return emailProperty;
        return null;
    }

    protected final void remove() {
        removeCallback.remove(this);
    }

    public interface Factory {
        RealUserImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            RealListPersistedImpl.RemoveCallback<RealUserImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<User.Data, RealUserImpl> {

        private final RealUserImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealUserImpl create(Logger logger, User.Data data, RealListPersistedImpl.RemoveCallback<RealUserImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}
