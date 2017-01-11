package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base class for all user
 */
public final class RealUserImpl
        extends RealObject<User.Data, User.Listener<? super RealUserImpl>>
        implements RealUser<RealCommandImpl, RealPropertyImpl<String>, RealUserImpl> {

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealPropertyImpl<String> emailProperty;

    private final RemoveCallback<RealUserImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealUserImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        @Assisted RemoveCallback<RealUserImpl> removeCallback,
                        ListenersFactory listenersFactory,
                        RealCommandImpl.Factory commandFactory,
                        RealParameterImpl.Factory parameterFactory,
                        RealPropertyImpl.Factory propertyFactory,
                        RealValueImpl.Factory valueFactory,
                        TypeRepository typeRepository) {
        super(logger, true, new User.Data(id, name, description), listenersFactory);
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
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        emailProperty.init(ChildUtil.name(name, User.EMAIL_ID), connection);
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

    protected final void remove() {
        removeCallback.removeUser(this);
    }

    public interface Factory {
        RealUserImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            RemoveCallback<RealUserImpl> removeCallback);
    }
}
