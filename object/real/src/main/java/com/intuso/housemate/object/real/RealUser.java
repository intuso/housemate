package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.real.factory.user.RealUserOwner;
import com.intuso.housemate.object.real.impl.type.Email;
import com.intuso.housemate.object.real.impl.type.EmailType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealUser
        extends RealObject<UserData, HousemateData<?>, RealObject<?, ? ,?, ?>, UserListener>
        implements User<RealCommand, RealProperty<Email>> {

    private final RealCommand remove;
    private final RealProperty<Email> emailProperty;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealUser(final Log log, ListenersFactory listenersFactory,
                    @Assisted UserData data, @Assisted final RealUserOwner owner) {
        super(log, listenersFactory, data);
        this.remove = new RealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the user", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.removeUser(RealUser.this);
            }
        };
        this.emailProperty = new RealProperty<Email>(log, listenersFactory, EMAIL_ID, EMAIL_ID, "The user's email address", new EmailType(log, listenersFactory), (Email)null);
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

}
