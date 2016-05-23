package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.api.internal.type.Email;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger logger = LoggerFactory.getLogger(EmailType.class);

    /**
     * @param listenersFactory
     */
    @Inject
    public EmailType(ListenersFactory listenersFactory, Email.Factory emailFactory) {
        super(logger, ID, NAME, "Email address of the form <username>@<host>", listenersFactory, ".+@.+\\..+", emailFactory);
    }

    @Override
    public Instance serialise(Email email) {
        return email == null ? null : new Instance(email.getEmail());
    }

    @Override
    public Email deserialise(Instance instance) {
        return instance == null || instance.getValue() == null ? null : new Email(instance.getValue());
    }
}
