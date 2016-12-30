package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class FeatureDriverType extends FactoryType<FeatureDriver.Factory<?>> {

    public final static String TYPE_ID = "feature-factory";
    public final static String TYPE_NAME = "Feature Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new feature";

    @Inject
    protected FeatureDriverType(@Type Logger logger, ListenersFactory listenersFactory,
                                RealOptionImpl.Factory optionFactory, RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, listenersFactory, optionFactory, optionsFactory);
    }
}
