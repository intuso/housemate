package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 01/05/14
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
public class EmailType extends RealRegexType {

    public final static String ID = "email";
    public final static String NAME = "Email";
    public final static String DESCRIPTION = "Email address of the form <username>@<host>";
    public final static String REGEX = ".+@.+\\..+";

    /**
     * @param listenersFactory
     */
    @Inject
    public EmailType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, ID), ID, NAME, DESCRIPTION, REGEX, listenersFactory);
    }
}
