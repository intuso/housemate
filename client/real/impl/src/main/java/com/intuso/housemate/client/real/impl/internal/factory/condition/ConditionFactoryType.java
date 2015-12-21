package com.intuso.housemate.client.real.impl.internal.factory.condition;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.impl.internal.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 19/03/15.
 */
public class ConditionFactoryType extends FactoryType<ConditionDriver.Factory<?>> {

    public final static String TYPE_ID = "condition-factory";
    public final static String TYPE_NAME = "Condition Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new conditions";

    private final static Logger logger = LoggerFactory.getLogger(ConditionFactoryType.class);

    @Inject
    protected ConditionFactoryType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
