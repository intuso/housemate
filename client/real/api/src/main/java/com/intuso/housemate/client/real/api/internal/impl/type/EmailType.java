package com.intuso.housemate.client.real.api.internal.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 01/05/14
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
public class EmailType extends RealRegexType<Email> {

    public final static String ID = "email";
    public final static String NAME = "Email";

    /**
     * @param log              the log
     * @param listenersFactory
     */
    @Inject
    public EmailType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, ID, NAME, "Email address of the form <username>@<host>", 1, 1, ".+@.+\\..+");
    }

    @Override
    public TypeInstance serialise(Email email) {
        return email == null ? null : new TypeInstance(email.getEmail());
    }

    @Override
    public Email deserialise(TypeInstance instance) {
        return instance == null || instance.getValue() == null ? null : new Email(instance.getValue());
    }
}
