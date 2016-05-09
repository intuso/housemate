package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.api.internal.type.Email;
import com.intuso.housemate.client.real.impl.internal.type.EmailType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Base class for all user
 */
public final class RealUserImpl
        extends RealObject<User.Data, User.Listener<? super RealUserImpl>>
        implements RealUser<RealCommandImpl, RealPropertyImpl<Email>, RealUserImpl> {

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealPropertyImpl<Email> emailProperty;

    private final RemoveCallback<RealUserImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param data the user's data
     * @param listenersFactory
     */
    @Inject
    public RealUserImpl(@Assisted final Logger logger,
                        @Assisted User.Data data,
                        ListenersFactory listenersFactory,
                        @Assisted RemoveCallback<RealUserImpl> removeCallback) {
        super(logger, data, listenersFactory);
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the user"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealUserImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the user"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                remove();
            }
        };
        this.emailProperty = new RealPropertyImpl<>(ChildUtil.logger(logger, User.EMAIL_ID),
                new Property.Data(User.EMAIL_ID, User.EMAIL_ID, User.EMAIL_ID),
                listenersFactory,
                new EmailType(listenersFactory));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        emailProperty.init(ChildUtil.name(name, User.EMAIL_ID), session);
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
    public RealPropertyImpl<Email> getEmailProperty() {
        return emailProperty;
    }

    protected final void remove() {
        removeCallback.removeUser(this);
    }

    public interface Factory {
        RealUserImpl create(Logger logger, User.Data data, RemoveCallback<RealUserImpl> removeCallback);
    }
}
