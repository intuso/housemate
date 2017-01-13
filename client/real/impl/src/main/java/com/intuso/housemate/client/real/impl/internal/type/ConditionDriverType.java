package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class ConditionDriverType extends FactoryType<ConditionDriver.Factory<?>> {

    public final static String TYPE_ID = "condition-factory";
    public final static String TYPE_NAME = "Condition Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new condition";

    @Inject
    protected ConditionDriverType(@Type Logger logger, ListenersFactory listenersFactory,
                                  RealOptionImpl.Factory optionFactory, RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION,
                new TypeSpec(Types.newParameterizedType(PluginDependency.class, ConditionDriver.class)),
                listenersFactory, optionFactory, optionsFactory);
    }
}
