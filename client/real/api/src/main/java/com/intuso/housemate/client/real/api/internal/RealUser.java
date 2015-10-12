package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.impl.type.Email;
import com.intuso.housemate.client.real.api.internal.impl.type.EmailType;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.User;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealUser
        extends RealObject<UserData, HousemateData<?>, RealObject<?, ? ,?, ?>, User.Listener>
        implements User<RealCommand, RealProperty<Email>> {

    private final RealCommand remove;
    private final RealProperty<Email> emailProperty;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealUser(final Log log,
                    ListenersFactory listenersFactory,
                    @Assisted UserData data,
                    @Assisted final RemovedListener removedListener) {
        super(log, listenersFactory, data);
        this.remove = new RealCommand(log, listenersFactory, UserData.REMOVE_ID, UserData.REMOVE_ID, "Remove the user", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removedListener.userRemoved(RealUser.this);
            }
        };
        this.emailProperty = new RealProperty<>(log, listenersFactory, UserData.EMAIL_ID, UserData.EMAIL_ID, "The user's email address", new EmailType(log, listenersFactory), (Email)null);
        addChild(remove);
        addChild(emailProperty);
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealProperty<Email> getEmailProperty() {
        return emailProperty;
    }

    public interface RemovedListener {
        void userRemoved(RealUser user);
    }

    public interface Factory {
        RealUser create(UserData data, RemovedListener removedListener);
    }
}
