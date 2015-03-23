package com.intuso.housemate.object.real.factory.condition;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class ConditionFactoryType extends FactoryType<RealConditionFactory<?>> {

    public final static String TYPE_ID = "condition-factory";
    public final static String TYPE_NAME = "Condition Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new conditions";

    @Inject
    protected ConditionFactoryType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
